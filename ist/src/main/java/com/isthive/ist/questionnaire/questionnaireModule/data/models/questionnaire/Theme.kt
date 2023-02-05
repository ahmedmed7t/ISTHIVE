package com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire

internal data class Theme(
    val ActionCardStyle: ActionCardStyle,
    val EndScreenMsg: Any,
    val EndScreenMsgStyle: EndScreenMsgStyle,
    val FocusColor: String,
    val FocusFontColor: String,
    val InputAnswerStyle: InputAnswerStyle,
    val NextButton: NextButton,
    val PreviousButton: PreviousButton,
    val QuestionChoicesStyle: QuestionChoicesStyle,
    val QuestionTitleStyle: QuestionTitleStyle,
    val RateusButton: RateusButton,
    val StartScreenMsg: Any,
    val StartScreenMsgStyle: StartScreenMsgStyle,
    val SubmitButton: SubmitButton,
    val SurveyBackgroundColor: String,
    val SurveyLogoStyle: SurveyLogoStyle,
    val SurveyTitleStyle: SurveyTitleStyle,
    val TakeSurveyButton: TakeSurveyButton,
    val ThankyouCloseButton: ThankyouCloseButton,
    val ThankyouMessage: ThankyouMessage,
    val TitleOrientation: String,
    val WelcomeMessage: WelcomeMessage?
)