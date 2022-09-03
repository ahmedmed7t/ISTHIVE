package com.ist.isthiveapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.isthive.ist.QuestionnaireBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var button: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.button)

        button.setOnClickListener {
            QuestionnaireBuilder().
            openQuestionnaire(this)
        }
    }
}