package com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.natiqhaciyef.prodocument.databinding.RecyclerCategoryItemBinding
import com.natiqhaciyef.core.base.ui.BaseRecyclerViewAdapter
import com.natiqhaciyef.core.model.CategoryItem

class ShareCategoryAdapter(
    dataList: MutableList<CategoryItem>
) : BaseRecyclerViewAdapter<CategoryItem, RecyclerCategoryItemBinding>(dataList) {

    var onClickAction: (String) -> Unit = {

    }

    override val binding: (Context, ViewGroup, Boolean) -> RecyclerCategoryItemBinding =
        { context, viewGroup, b ->
            RecyclerCategoryItemBinding.inflate(LayoutInflater.from(context), viewGroup, b)
        }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = list[position]
        with(holder.binding) {
            categoryTitle.text = item.title
            categoryIcon.setImageResource(item.iconId)

            if (item.size != null && !item.sizeType.isNullOrEmpty()) {
                val size = String.format("%.1f", item.size)
                sizeOfItemText.visibility = View.VISIBLE
                sizeOfItemText.text = "$size (${item.sizeType?.uppercase()})"
            } else {
                sizeOfItemText.visibility = View.GONE
            }
        }

        holder.itemView.setOnClickListener { onClickAction.invoke(item.type) }
    }
}