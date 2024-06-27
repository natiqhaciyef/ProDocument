package com.natiqhaciyef.prodocument.ui.view.main.profile.params.graphs.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.natiqhaciyef.common.model.MappedGraphDetailModel
import com.natiqhaciyef.core.base.ui.BaseRecyclerViewAdapter
import com.natiqhaciyef.common.R
import com.natiqhaciyef.prodocument.databinding.RecyclerGraphItemBinding

class GraphDetailsAdapter(
    val ctx: Context,
    statistics: MutableList<MappedGraphDetailModel>
) : BaseRecyclerViewAdapter<MappedGraphDetailModel, RecyclerGraphItemBinding>(statistics) {
    override val binding: (Context, ViewGroup, Boolean) -> RecyclerGraphItemBinding
        get() = { ctx, vg, bool ->
            RecyclerGraphItemBinding.inflate(LayoutInflater.from(ctx), vg, bool)
        }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = list[position]
        with(holder.binding) {
            graphCategoryTitle.text = item.title
            graphCategoryIcon.setImageResource(item.icon)
            graphCategoryPercentage.text = "${item.percentage} %"
        }
    }
}