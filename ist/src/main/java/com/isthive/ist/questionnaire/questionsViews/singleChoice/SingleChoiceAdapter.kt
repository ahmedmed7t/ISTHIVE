package com.isthive.ist.questionnaire.questionsViews.singleChoice

import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Choice
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.ChoiceType

internal class SingleChoiceAdapter constructor(
    private val choices: ArrayList<Choice>,
    private val isModernMode: Boolean
) : RecyclerView.Adapter<SingleChoiceAdapter.SingleViewHolder>() {

    private var lastSelectedPosition: Int? = null

    inner class SingleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val choiceContainer: ConstraintLayout =
            itemView.findViewById(R.id.singleChoiceListItemContainer)
        val choiceRadioButton: RadioButton = itemView.findViewById(R.id.singleChoiceListItemRadio)
        val choiceTitle: TextView = itemView.findViewById(R.id.singleChoiceListItemTitle)
        val otherEditText: EditText = itemView.findViewById(R.id.singleChoiceListItemOtherEditText)
        val choiceRightIcon: AppCompatImageView =
            itemView.findViewById(R.id.singleChoiceListItemRightMark)
        val choiceDataContainer: ConstraintLayout =
            itemView.findViewById(R.id.singleChoiceListItemChoiceContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleViewHolder {
        return SingleViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.single_choice_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SingleViewHolder, position: Int) {
        holder.choiceTitle.text = choices[position].Title
        holder.choiceRightIcon.visibility = View.GONE
        holder.choiceRadioButton.isChecked = false

        holder.choiceContainer.setOnClickListener {
            selectChoice(position)
        }
        holder.choiceRadioButton.setOnClickListener {
            selectChoice(position)
        }
        holder.choiceRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked && choices[position].Type == ChoiceType.Other_Choice) {
                holder.otherEditText.visibility = View.GONE
            }
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

    private fun viewModernMode(holder: SingleViewHolder, position: Int) {
        holder.choiceRadioButton.visibility = View.GONE
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
                ContextCompat.getColorStateList(holder.itemView.context, R.color.light_blue)
        }
    }

    private fun viewClassicMode(holder: SingleViewHolder, position: Int) {
        holder.choiceRadioButton.visibility = View.VISIBLE
        holder.choiceRightIcon.visibility = View.GONE
        holder.otherEditText.visibility = View.GONE
        holder.choiceDataContainer.background = null

        holder.choiceRadioButton.isChecked = choices[position].isSelected

        if (choices[position].isSelected && choices[position].Type == ChoiceType.Other_Choice)
            holder.otherEditText.visibility = View.VISIBLE
    }

    override fun getItemCount() = choices.size

    private fun selectChoice(position: Int) {
        choices.forEachIndexed { index, element ->
            element.isSelected = position == index
        }
        android.os.Handler(Looper.getMainLooper()).post {
            Log.v("Medhat", "new pos is $position")
            Log.v("Medhat", "last pos is $lastSelectedPosition")
            Log.v("Medhat", "new pos is ${choices[position].isSelected}")
            lastSelectedPosition?.let {
                Log.v("Medhat", "last pos is ${choices[it].isSelected}")
            }

            notifyItemChanged(position)
            lastSelectedPosition?.let {
                notifyItemChanged(it)
            }
            lastSelectedPosition = position
        }
    }
}