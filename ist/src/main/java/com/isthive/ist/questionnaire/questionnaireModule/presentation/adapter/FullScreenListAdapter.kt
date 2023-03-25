package com.isthive.ist.questionnaire.questionnaireModule.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Answer
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Question
import com.isthive.ist.questionnaire.questionsViews.BaseQuestionView

internal class FullScreenListAdapter(
    private var questions: ArrayList<BaseQuestionView>
) : RecyclerView.Adapter<FullScreenListAdapter.FullScreenViewHolder>() {

    var welcomeMessage: String? = null

    internal inner class FullScreenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val container: ConstraintLayout = itemView.findViewById(R.id.fullScreenItemContainer)
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
            removeAllViews()
            if ((questions[position]).parent != null)
                ((questions[position]).parent as ViewGroup).removeAllViews()
            addView(questions[position])

            welcomeMessage?.let {
                if(position == 0 ){
                    questions[position].showWelcomeMessage(it)
                }
            }
        }
    }

    override fun getItemCount() = questions.size

    fun updateQuestionsList(questions: ArrayList<BaseQuestionView>) {
        this.questions = questions
    }

    fun appendQuestions(newQuestions: ArrayList<BaseQuestionView>, question: Question) {
        val questionIndex = getQuestionIndex(question)
        if (questionIndex == questions.size - 1) {
            questions.addAll(newQuestions)
        } else {
            for (count in questionIndex + 1 until questions.size) {
                questions.removeLast()
            }
            questions.addAll(newQuestions)
        }
        notifyDataSetChanged()
    }

    private fun getQuestionIndex(question: Question): Int {
        for ((index, item) in questions.withIndex()) {
            if (item.question.QuestionGUID == question.QuestionGUID)
                return index
        }
        return 0
    }

    fun checkAnswersAvailability(): Boolean{
        var allQuestionsValid = true
        for(item in questions){
            if(!item.isAnswerValid) {
                allQuestionsValid = false
                item.showError()
            }
        }
        return allQuestionsValid
    }

    fun collectAnswers(): ArrayList<Answer> {
        val answers = arrayListOf<Answer>()
        for(item in questions){
            item.getAnswer()?.let { answers.add(it) }
        }
        return answers
    }

    fun showWelcomeMessageForFirstItem(title: String?){
        welcomeMessage = title
        notifyItemChanged(0)
    }
}