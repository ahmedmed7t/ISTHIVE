package com.isthive.ist

import android.app.Activity
import android.content.Intent
import com.isthive.ist.app.helper.LocaleHelper
import com.isthive.ist.questionnaire.questionnaireModule.presentation.QuestionnaireActivity

class QuestionnaireBuilder {

    private var userName = ""
    private var password = ""

    fun setUserName(userName: String): QuestionnaireBuilder{
        this.userName = userName
        return this
    }

    fun setPassword(password: String): QuestionnaireBuilder{
        this.password = password
        return this
    }

    fun openQuestionnaire(activity: Activity){
        LocaleHelper.defaultLanguage = "en"
        val intent = Intent(activity, QuestionnaireActivity::class.java)
        if(userName.isNotBlank())
            intent.putExtra(QuestionnaireActivity.QUESTIONNAIRE_USER_NAME, userName)
        if(password.isNotBlank())
            intent.putExtra(QuestionnaireActivity.QUESTIONNAIRE_PASSWORD, password)
        activity.startActivity(intent)
    }
}