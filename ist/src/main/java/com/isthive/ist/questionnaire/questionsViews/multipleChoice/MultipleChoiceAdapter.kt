package com.isthive.ist.questionnaire.questionsViews.multipleChoice

import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Choice
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.ChoiceType

internal class MultipleChoiceAdapter constructor(
    private val choices: ArrayList<Choice>,
    private val isModernMode: Boolean,
    private val handlerMultiple: MultipleChoiceAdapterHandler
) : RecyclerView.Adapter<MultipleChoiceAdapter.MultipleViewHolder>() {

    var otherText = ""
    var otherIndex = -1
    inner class MultipleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val choiceContainer: ConstraintLayout =
            itemView.findViewById(R.id.multipleChoiceListItemContainer)
        val choiceCheckBox: AppCompatCheckBox =
            itemView.findViewById(R.id.multipleChoiceListItemCheck)
        val choiceTitle: TextView = itemView.findViewById(R.id.multipleChoiceListItemTitle)
        val otherEditText: EditText =
            itemView.findViewById(R.id.multipleChoiceListItemOtherEditText)
        val choiceRightIcon: AppCompatImageView =
            itemView.findViewById(R.id.multipleChoiceListItemRightMark)
        val choiceDataContainer: ConstraintLayout =
            itemView.findViewById(R.id.multipleChoiceListItemChoiceContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultipleViewHolder {
        return MultipleViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.multiple_choice_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MultipleViewHolder, position: Int) {
        holder.choiceTitle.text = choices[position].Title
        holder.choiceRightIcon.visibility = View.GONE
        holder.choiceCheckBox.isChecked = false

        holder.choiceContainer.setOnClickListener {
            selectChoice(position)
        }
        holder.choiceCheckBox.setOnClickListener {
            selectChoice(position)
        }
        holder.choiceCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked && choices[position].Type == ChoiceType.Other_Choice) {
                holder.otherEditText.visibility = View.GONE
            }
        }

        holder.otherEditText.addTextChangedListener {
            otherText = it.toString()
            otherIndex = position
        }

        when (isModernMode) {
            true -> {
                viewModernMode(holder, position)
            }
            false -> {
                viewClassicMode(holder, position)
            }
        }
    }

    private fun viewModernMode(holder: MultipleViewHolder, position: Int) {
        holder.choiceCheckBox.visibility = View.GONE
        holder.choiceRightIcon.visibility = View.GONE
        holder.otherEditText.visibility = View.GONE
        holder.choiceDataContainer.background =
            ContextCompat.getDrawable(holder.itemView.context, R.drawable.rounded_solid)

        if (choices[position].isSelected) {
            if (choices[position].Type == ChoiceType.Other_Choice)
                holder.otherEditText.visibility = View.VISIBLE
            holder.choiceRightIcon.visibility = View.VISIBLE
            holder.choiceTitle.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
                )
            )
            holder.choiceDataContainer.backgroundTintList =
                ContextCompat.getColorStateList(holder.itemView.context, R.color.blue)
        } else {
            holder.choiceTitle.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.blue
                )
            )
            holder.choiceDataContainer.backgroundTintList =
                ContextCompat.getColorStateList(holder.itemView.context, R.color.white_blue)
        }
    }

    private fun viewClassicMode(holder: MultipleViewHolder, position: Int) {
        holder.choiceCheckBox.visibility = View.VISIBLE
        holder.choiceRightIcon.visibility = View.GONE
        holder.otherEditText.visibility = View.GONE
        holder.choiceDataContainer.background = null

        holder.choiceCheckBox.isChecked = choices[position].isSelected

        if (choices[position].isSelected && choices[position].Type == ChoiceType.Other_Choice)
            holder.otherEditText.visibility = View.VISIBLE
    }

    override fun getItemCount() = choices.size

    private fun selectChoice(position: Int) {
        if (choices[position].isSelected)
            handlerMultiple.onChoiceUnSelected(position)
        else
            handlerMultiple.onChoiceSelected(position)

        choices[position].isSelected = !choices[position].isSelected
        android.os.Handler(Looper.getMainLooper()).post {
            notifyItemChanged(position)
        }
    }
}