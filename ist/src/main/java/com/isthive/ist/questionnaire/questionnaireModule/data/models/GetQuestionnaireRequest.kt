package com.isthive.ist.questionnaire.questionnaireModule.data.models

internal data class GetQuestionnaireRequest(
    val AgentID: String = "AgentInApp-01",
    val AgentName: String = "AgentInApp-01",
    val Channel: String = "InApp",
    val CustomerCIC: String = "0000000154154154",
    val CustomerEmail: String? = null,
    val CustomerName: String = "Osama",
    val CustomerPhoneNumber: String = "0125455141412",
    val DispositionCodes: List<Any> = listOf(),
    val InteractionID: String = "Int-0001",
    val Language: String = "en",
    val SkillGroup: String = "Servicing & Behaviour",
    val SourceID: String = "00001251"
)