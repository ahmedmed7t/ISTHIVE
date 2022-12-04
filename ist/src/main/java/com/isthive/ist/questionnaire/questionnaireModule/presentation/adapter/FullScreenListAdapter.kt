package com.isthive.ist.questionnaire.questionnaireModule.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.allViews
import androidx.recyclerview.widget.RecyclerView
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionsViews.BaseQuestionView

class FullScreenListAdapter(
    private var questions: ArrayList<BaseQuestionView>
) : RecyclerView.Adapter<FullScreenListAdapter.FullScreenViewHolder>() {

    inner class FullScreenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val container = itemView.findViewById<ConstraintLayout>(R.id.fullScreenItemContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FullScreenViewHolder {
        return FullScreenViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.full_screen_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FullScreenViewHolder, position: Int) {
        questions[position].layoutParams = ViewGroup.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        holder.container.apply {
            for (item in allViews)
                removeView(item)
            addView(questions[position])
        }
    }

    override fun getItemCount() = questions.size

    fun updateQuestionsList(questions: ArrayList<BaseQuestionView>){
        this.questions = questions
    }
}