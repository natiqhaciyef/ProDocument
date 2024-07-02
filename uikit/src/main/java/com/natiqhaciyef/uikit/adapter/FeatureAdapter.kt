package com.natiqhaciyef.uikit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.natiqhaciyef.core.base.ui.BaseRecyclerViewAdapter
import com.natiqhaciyef.uikit.databinding.RecyclerSubscriptionFeatureItemBinding

class FeatureAdapter(
    list: MutableList<String>,
) : BaseRecyclerViewAdapter<String, RecyclerSubscriptionFeatureItemBinding>(list) {
    override val binding: (Context, ViewGroup, Boolean) -> RecyclerSubscriptionFeatureItemBinding =
        { context, viewGroup, bool ->
            RecyclerSubscriptionFeatureItemBinding.inflate(LayoutInflater.from(context), viewGroup, bool)
        }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.binding.featureText.text = list[position]
    }
}