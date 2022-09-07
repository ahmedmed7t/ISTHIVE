package com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire

internal class Answer(
    var QuestionGUID: String,
    var QuestionID: Int,
    var SelectedChoices: ArrayList<AnswerChoice>? = arrayListOf(),
    var NumberResponse: Int
){
    fun getSingleChoiceAnswer(): AnswerChoice {
        if (SelectedChoices != null && SelectedChoices!!.count() > 0) {
            return SelectedChoices!![0]
        }
        return AnswerChoice("",0,"")
    }
}
