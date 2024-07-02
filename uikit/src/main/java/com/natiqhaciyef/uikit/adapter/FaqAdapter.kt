package com.natiqhaciyef.uikit.adapter

import android.animation.LayoutTransition
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.natiqhaciyef.common.model.FaqModel
import com.natiqhaciyef.core.base.ui.BaseRecyclerViewAdapter
import com.natiqhaciyef.uikit.databinding.RecyclerFaqItemBinding

class FaqAdapter(faqList: MutableList<FaqModel>) :
    BaseRecyclerViewAdapter<FaqModel, RecyclerFaqItemBinding>(faqList) {
    private var isExpanded: Boolean = false
    override val binding: (Context, ViewGroup, Boolean) -> RecyclerFaqItemBinding =
        { ctx, vg, bool ->
            RecyclerFaqItemBinding.inflate(LayoutInflater.from(ctx), vg, bool)
        }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = list[position]

        with(holder.binding) {
            questionTitle.text = item.title
            questionDetails.text = item.description
            TransitionManager.beginDelayedTransition(cardLayout, AutoTransition())
            cardLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

            expandIcon.setOnClickListener { onExpandIconClickAction(holder.binding) }
        }

        holder.itemView.setOnClickListener { onExpandIconClickAction(holder.binding) }
    }

    private fun onExpandIconClickAction(binding: RecyclerFaqItemBinding) {
        isExpanded = !isExpanded
        with(binding){
            questionDetails.visibility = if (isExpanded) View.VISIBLE else View.GONE
            simpleLine.visibility = if (isExpanded) View.VISIBLE else View.GONE
            expandIcon.rotation = if(isExpanded) 0f else 270f
        }
    }
}