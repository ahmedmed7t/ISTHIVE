package com.isthive.ist.questionnaire.provider

import android.content.Context
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Answer
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Question
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.QuestionType
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.SkipLogic
import com.isthive.ist.questionnaire.questionsViews.BaseQuestionView
import com.isthive.ist.questionnaire.questionsViews.fcr.FCRQuestion
import com.isthive.ist.questionnaire.questionsViews.ces.NumericCESQuestion
import com.isthive.ist.questionnaire.questionsViews.numeric_ces.NumCESQuestion
import com.isthive.ist.questionnaire.questionsViews.csat.CSATQuestion
import com.isthive.ist.questionnaire.questionsViews.emoji.EmojiQuestion
import com.isthive.ist.questionnaire.questionsViews.multipleChoice.MultipleChoiceQuestion
import com.isthive.ist.questionnaire.questionsViews.nps.NPSQuestion
import com.isthive.ist.questionnaire.questionsViews.numeric_csat.NumericCSATQuestion
import com.isthive.ist.questionnaire.questionsViews.rating.RatingQuestion
import com.isthive.ist.questionnaire.questionsViews.singleChoice.SingleChoiceQuestion
import com.isthive.ist.questionnaire.questionsViews.sliding.SlidingQuestion

internal class QuestionProvider(
    var questions: ArrayList<Question>,
    var skipLogic: ArrayList<SkipLogic>?
) {

    var questionIndex = -1

    fun getNextQuestion(answer: Answer, context: Context): BaseQuestionView {
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

        return loadViewRelevantToQuestion(questions[questionIndex], context)
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

    private fun getNextQuestionIndexFromSkipLogic(question: Question, answer: Answer): Int {
        var nextQuestionGuid = ""
        var nextQuestionIndex = 0
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
                        (item.MinValue <= answer.NumberResponse &&
                                item.MaxValue >= answer.NumberResponse)
                    ) {
                        nextQuestionGuid = item.SkipToQuestionGUID
                        nextQuestionIndex = nextQuestionGuidIsNull(
                            question, nextQuestionGuid
                        )
                        return nextQuestionIndex

                    } else if (item.QChoiceGUID.isNotEmpty() &&
                        (item.QChoiceGUID == answer.getSingleChoiceAnswer().ChoiceGuid)
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
                context.applicationContext,
                question
            )
            QuestionType.List_question -> NumCESQuestion(context.applicationContext, question)
            QuestionType.Date_question -> NumCESQuestion(context.applicationContext, question)
            QuestionType.Slide_question -> SlidingQuestion(context.applicationContext, question)
            QuestionType.Star_question -> RatingQuestion(context.applicationContext, question)
            QuestionType.NPS -> NPSQuestion(context.applicationContext, question)
            QuestionType.Text_input -> NumCESQuestion(context.applicationContext, question)
            QuestionType.Number_input -> NumCESQuestion(context.applicationContext, question)
            QuestionType.Email_input -> NumCESQuestion(context.applicationContext, question)
            QuestionType.Phone_number_input -> NumCESQuestion(
                context.applicationContext, question
            )
            QuestionType.Postal_code_input -> NumCESQuestion(
                context.applicationContext, question
            )
            QuestionType.URL_input -> NumCESQuestion(context.applicationContext, question)
            QuestionType.Single_choice -> SingleChoiceQuestion(context.applicationContext, question)
            QuestionType.EmojiHiveCFM_Mobile_API -> EmojiQuestion(
                context.applicationContext, question
            )
            QuestionType.Image_MCQ -> NumCESQuestion(context.applicationContext, question)
            QuestionType.Image_Single_Choice -> NumCESQuestion(
                context.applicationContext, question
            )
            QuestionType.CSAT -> CSATQuestion(context.applicationContext, question)
            QuestionType.FCR -> FCRQuestion(context.applicationContext, question)
            QuestionType.CES -> NumericCESQuestion(context.applicationContext, question)
            QuestionType.Numeric_CSAT -> NumericCSATQuestion(context.applicationContext, question)
            QuestionType.Numeric_CES -> NumCESQuestion(context.applicationContext, question)
        }
    }
}