package com.natiqhaciyef.uikit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.natiqhaciyef.common.model.LanguageModel
import com.natiqhaciyef.core.base.ui.BaseRecyclerViewAdapter
import com.natiqhaciyef.common.R
import com.natiqhaciyef.uikit.databinding.RecyclerLanguageItemBinding


class LanguageAdapter(
    private val ctx: Context,
    languages: MutableList<LanguageModel>
): BaseRecyclerViewAdapter<LanguageModel, RecyclerLanguageItemBinding>(languages) {
    var onClick: (LanguageModel) -> Unit  = {

    }

    override val binding: (Context, ViewGroup, Boolean) -> RecyclerLanguageItemBinding = { ctx, vg, bool ->
        RecyclerLanguageItemBinding.inflate(LayoutInflater.from(ctx), vg, false)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = list[position]

        with(holder.binding){
            if (item.title.isNotEmpty())
                languageNameTag.text = ctx.getString(R.string.lang_tag_container, item.title)
            else
                languageNameTag.text = ctx.getString(R.string.lang_tag_container, "en")
            languageNameDetailed.text = item.detailedName
        }

        holder.itemView.setOnClickListener { onClick.invoke(item) }
    }
}