package com.isthive.ist.questionnaire.questionnaireModule.presentation

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.isthive.ist.R
import com.isthive.ist.questionnaire.provider.QuestionProvider
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.QuestionType
import com.isthive.ist.questionnaire.questionsViews.fcr.FCRQuestion
import com.isthive.ist.questionnaire.viewContainers.BottomSheetContainer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class QuestionnaireActivity : AppCompatActivity() {
    private val viewModel: QuestionnaireViewModel by viewModels()

    private lateinit var questionProvider: QuestionProvider
    private lateinit var button: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionare)

        button = findViewById(R.id.getToken)
        listenToViewModelValues()
        button.setOnClickListener {
//            BottomSheetContainer()
//                .mainView(NPSQuestion(this, null))
//                .show(supportFragmentManager, "tag")
            viewModel.generateToken("InAppUser","InApp2021")
        }
    }

    private fun listenToViewModelValues(){
        viewModel.questionnaireState.observe(this){
            when(it){
                is QuestionnaireUiState.Success -> {
                    questionProvider = QuestionProvider(it.survey.Questions, it.survey.SkipLogics)
                    for (item in it.survey.Questions){
                        if(item.QuestionType == QuestionType.CSAT){
                            BottomSheetContainer()
                                .mainView(FCRQuestion(this, it.survey.Questions[0]))
                                .show(supportFragmentManager, "tag")
                            break
                        }
                    }
                }
            }
        }
    }

    internal companion object{
        const val QUESTIONNAIRE_USER_NAME = "QUESTIONNAIRE_USER_NAME"
        const val QUESTIONNAIRE_PASSWORD = "QUESTIONNAIRE_PASSWORD"
    }
}