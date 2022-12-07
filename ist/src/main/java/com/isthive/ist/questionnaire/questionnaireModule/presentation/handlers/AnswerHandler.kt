package com.isthive.ist.questionnaire.questionnaireModule.presentation.handlers

import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Answer
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Question

internal interface AnswerHandler {
    fun onAnswerClicked(answer: Answer, question: Question?)
}