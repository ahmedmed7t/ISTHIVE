package com.isthive.ist.questionnaire.provider

import android.content.Context
import android.util.Log
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

internal class QuestionProvider(
    var questions: ArrayList<Question>,
    var skipLogic: ArrayList<SkipLogic>?
) {

    var questionIndex = -1

    fun getNextQuestion(answer: Answer?, context: Context): BaseQuestionView? {
        while (questionIndex < questions.count()) {
            if (questionIndex == -1) {
                questionIndex += 1
                return loadViewRelevantToQuestion(questions[questionIndex], context)
            }
            val question = questions[questionIndex]
            if (isQuestionHaveSkipLogic(question.QuestionGUID)) {
                questionIndex = getNextQuestionIndexFromSkipLogic(question, answer)
                break
            } else {
                questionIndex += 1
                break
            }
        }
        if (questionIndex == questions.size)
            return null
        return loadViewRelevantToQuestion(questions[questionIndex], context)
    }

    fun isLastQuestion(question: Question?): Boolean {
        var indexOfQuestion = -1
        for ((index, item) in questions.withIndex()) {
            if (question?.QuestionGUID == item.QuestionGUID) {
                indexOfQuestion = index
                break
            }
        }
        Log.v("Medhat", "last index is $indexOfQuestion of questions size ${questions.size}")
        return indexOfQuestion == questions.size - 1
    }

    fun isFirstQuestion(question: Question?): Boolean {
        Log.v("Medhat", "first index is question $question ")
        Log.v("Medhat", "first index is question GUID ${question?.QuestionGUID} ")
        var indexOfQuestion = -1
        for ((index, item) in questions.withIndex()) {
            if (question?.QuestionGUID == item.QuestionGUID) {
                indexOfQuestion = index
                break
            }
        }

        Log.v("Medhat", "first index is $indexOfQuestion of questions size ${questions.size}")
        return indexOfQuestion == 0
    }

    fun updateQuestionIndex(question: Question?) {
        for ((index, item) in questions.withIndex()) {
            if (question?.QuestionGUID == item.QuestionGUID) {
                questionIndex = index
                break
            }
        }
    }

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
        return questionIndex + 1
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
                context.applicationContext,
                question
            )
            QuestionType.List_question -> NumericCESQuestion(context.applicationContext, question)
            QuestionType.Date_question -> NumericCESQuestion(context.applicationContext, question)
            QuestionType.Slide_question -> SlidingQuestion(context.applicationContext, question)
            QuestionType.Star_question -> RatingQuestion(context.applicationContext, question)
            QuestionType.NPS -> NPSQuestion(context.applicationContext, question)
            QuestionType.Text_input -> InputQuestion(context.applicationContext, question)
            QuestionType.Number_input -> InputQuestion(context.applicationContext, question)
            QuestionType.Email_input -> InputQuestion(context.applicationContext, question)
            QuestionType.Phone_number_input -> InputQuestion(
                context.applicationContext, question
            )
            QuestionType.Postal_code_input -> InputQuestion(
                context.applicationContext, question
            )
            QuestionType.URL_input -> InputQuestion(context.applicationContext, question)
            QuestionType.Single_choice -> SingleChoiceQuestion(context.applicationContext, question)
            QuestionType.EmojiHiveCFM_Mobile_API -> EmojiQuestion(
                context.applicationContext, question
            )
            QuestionType.Image_MCQ -> NumericCESQuestion(context.applicationContext, question)
            QuestionType.Image_Single_Choice -> NumericCESQuestion(
                context.applicationContext, question
            )
            QuestionType.CSAT -> CSATQuestion(context.applicationContext, question)
            QuestionType.FCR -> FCRQuestion(context.applicationContext, question)
            QuestionType.CES -> CESQuestion(context.applicationContext, question)
            QuestionType.Numeric_CSAT -> NumericCSATQuestion(context.applicationContext, question)
            QuestionType.Numeric_CES -> NumericCESQuestion(context.applicationContext, question)
        }
    }

    fun getProgressPercentage(): Int {
        var currentIndex = 0
        if (questionIndex > 0)
            currentIndex = questionIndex

        return (currentIndex + 1) / (questions.size) * 100
    }
}