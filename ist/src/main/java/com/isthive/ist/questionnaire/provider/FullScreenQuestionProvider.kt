package com.isthive.ist.questionnaire.provider

import android.content.Context
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Answer
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Question
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.QuestionType
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.SkipLogic
import com.isthive.ist.questionnaire.questionsViews.BaseQuestionView
import com.isthive.ist.questionnaire.questionsViews.ces.CESQuestion
import com.isthive.ist.questionnaire.questionsViews.csat.CSATQuestion
import com.isthive.ist.questionnaire.questionsViews.emoji.EmojiQuestion
import com.isthive.ist.questionnaire.questionsViews.fcr.FCRQuestion
import com.isthive.ist.questionnaire.questionsViews.input.InputQuestion
import com.isthive.ist.questionnaire.questionsViews.multipleChoice.MultipleChoiceQuestion
import com.isthive.ist.questionnaire.questionsViews.nps.NPSQuestion
import com.isthive.ist.questionnaire.questionsViews.numeric_ces.NumericCESQuestion
import com.isthive.ist.questionnaire.questionsViews.numeric_csat.NumericCSATQuestion
import com.isthive.ist.questionnaire.questionsViews.rating.RatingQuestion
import com.isthive.ist.questionnaire.questionsViews.singleChoice.SingleChoiceQuestion
import com.isthive.ist.questionnaire.questionsViews.sliding.SlidingQuestion

internal class FullScreenQuestionProvider(
    var questions: ArrayList<Question>,
    var skipLogic: ArrayList<SkipLogic>?
) {

    fun getInitialList(context: Context):  ArrayList<BaseQuestionView>{
        val initialQuestionsViews = arrayListOf<BaseQuestionView>()
        for (item in questions) {
            initialQuestionsViews.add(loadViewRelevantToQuestion(item, context))
            if (isQuestionHaveSkipLogic(item.QuestionGUID)) {
                return initialQuestionsViews
            }
        }
        return initialQuestionsViews
    }

    fun getNextListOfQuestions(answer: Answer, question: Question, context: Context): ArrayList<BaseQuestionView>{
        var currentIndex  = getNextQuestionIndexFromSkipLogic(question, answer)
        val firstQuestion = loadViewRelevantToQuestion(questions[currentIndex], context)
        val nextList = arrayListOf(firstQuestion)

        if(!isQuestionHaveSkipLogic(questions[currentIndex].QuestionGUID)) {
            for (i in currentIndex + 1 until questions.size) {
                nextList.add(loadViewRelevantToQuestion(questions[i], context))
                if (isQuestionHaveSkipLogic(questions[i].QuestionGUID)) {
                    return nextList
                }
            }
        }
        return nextList
    }

//    fun getQuestionIndex(question: Question): Int{
//        for((index, item) in questions.withIndex()){
//            if(item.QuestionGUID == question.QuestionGUID){
//                return index
//            }
//        }
//        return 0
//    }

    private fun isQuestionHaveSkipLogic(questionGUID: String): Boolean {
        skipLogic?.let {
            for (item in it) {
                if (questionGUID == item.QuestionGUID) {
                    return true
                }
            }
        }
        return false
    }

    private fun getNextQuestionIndexFromSkipLogic(question: Question, answer: Answer?): Int {
        var nextQuestionGuid: String
        var nextQuestionIndex: Int
        skipLogic?.let {
            for (item in it) {
                if (question.QuestionGUID == item.QuestionGUID) {
                    if (item.QChoiceGUID == "00000000-0000-0000-0000-000000000000" &&
                        (question.QuestionType != QuestionType.Multiple_choice_question ||
                                question.QuestionType != QuestionType.Date_question ||
                                question.QuestionType != QuestionType.Text_input ||
                                question.QuestionType != QuestionType.Number_input ||
                                question.QuestionType != QuestionType.Email_input ||
                                question.QuestionType != QuestionType.Phone_number_input ||
                                question.QuestionType != QuestionType.Postal_code_input ||
                                question.QuestionType != QuestionType.URL_input ||
                                question.QuestionType != QuestionType.Image_MCQ) &&
                        (item.MinValue <= (answer?.NumberResponse ?: 0) &&
                                item.MaxValue >= (answer?.NumberResponse ?: 0))
                    ) {
                        nextQuestionGuid = item.SkipToQuestionGUID
                        nextQuestionIndex = nextQuestionGuidIsNull(
                            question, nextQuestionGuid
                        )
                        return nextQuestionIndex

                    } else if (item.QChoiceGUID.isNotEmpty() &&
                        (item.QChoiceGUID == answer?.getSingleChoiceAnswer()?.ChoiceGuid)
                    ) {
                        nextQuestionGuid = item.SkipToQuestionGUID
                        nextQuestionIndex = nextQuestionGuidIsNull(question, nextQuestionGuid)
                        return nextQuestionIndex

                    } else if (question.QuestionType == QuestionType.Multiple_choice_question ||
                        question.QuestionType == QuestionType.Date_question ||
                        question.QuestionType == QuestionType.Text_input ||
                        question.QuestionType == QuestionType.Number_input ||
                        question.QuestionType == QuestionType.Email_input ||
                        question.QuestionType == QuestionType.Phone_number_input ||
                        question.QuestionType == QuestionType.Postal_code_input ||
                        question.QuestionType == QuestionType.URL_input ||
                        question.QuestionType == QuestionType.Image_MCQ
                    ) {
                        nextQuestionGuid = item.SkipToQuestionGUID
                        nextQuestionIndex = nextQuestionGuidIsNull(question, nextQuestionGuid)
                        return nextQuestionIndex
                    }
                }
            }
        }
        return 0
    }

    private fun nextQuestionGuidIsNull(question: Question, nextQuestionGuid: String?): Int {
        if (nextQuestionGuid.isNullOrBlank()) {
            return questions.count()
        }
        for ((index, question) in questions.withIndex()) {
            if (question.QuestionGUID == nextQuestionGuid) {
                return index
            }
        }
        return 0
    }

    private fun loadViewRelevantToQuestion(question: Question, context: Context): BaseQuestionView {
        // TODO check all NumericCESQuestion(context, question) to be removed from wrong cases
        return when (question.QuestionType) {
            QuestionType.Multiple_choice_question -> MultipleChoiceQuestion(
                context,
                question
            )
            QuestionType.List_question -> NumericCESQuestion(context, question)
            QuestionType.Date_question -> NumericCESQuestion(context, question)
            QuestionType.Slide_question -> SlidingQuestion(context, question)
            QuestionType.Star_question -> RatingQuestion(context, question)
            QuestionType.NPS -> NPSQuestion(context, question)
            QuestionType.Text_input -> InputQuestion(context, question)
            QuestionType.Number_input -> InputQuestion(context, question)
            QuestionType.Email_input -> InputQuestion(context, question)
            QuestionType.Phone_number_input -> InputQuestion(
                context, question
            )
            QuestionType.Postal_code_input -> InputQuestion(
                context, question
            )
            QuestionType.URL_input -> InputQuestion(context, question)
            QuestionType.Single_choice -> SingleChoiceQuestion(context, question)
            QuestionType.EmojiHiveCFM_Mobile_API -> EmojiQuestion(
                context, question
            )
            QuestionType.Image_MCQ -> NumericCESQuestion(context, question)
            QuestionType.Image_Single_Choice -> NumericCESQuestion(
                context, question
            )
            QuestionType.CSAT -> CSATQuestion(context, question)
            QuestionType.FCR -> FCRQuestion(context, question)
            QuestionType.CES -> CESQuestion(context, question)
            QuestionType.Numeric_CSAT -> NumericCSATQuestion(context, question)
            QuestionType.Numeric_CES -> NumericCESQuestion(context, question)
        }
    }
}