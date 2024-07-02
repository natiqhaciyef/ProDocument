package com.natiqhaciyef.uikit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import com.natiqhaciyef.common.constants.SIXTEEN
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.common.model.CategoryModel
import com.natiqhaciyef.core.base.ui.BaseRecyclerViewAdapter
import com.natiqhaciyef.common.R
import com.natiqhaciyef.uikit.databinding.RecyclerFaqCategoryItemBinding

class CategoryAdapter(
    private val ctx: Context,
    categoryList: MutableList<CategoryModel>
) : BaseRecyclerViewAdapter<CategoryModel, RecyclerFaqCategoryItemBinding>(categoryList) {
    override val binding: (Context, ViewGroup, Boolean) -> RecyclerFaqCategoryItemBinding
        get() = { ctx, vg, bool ->
            RecyclerFaqCategoryItemBinding.inflate(LayoutInflater.from(ctx), vg, bool)
        }

    var onClick: (CategoryModel) -> Unit = {

    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = list[position]

        holder.binding.also {
            when (position) {
                ZERO -> {
                    firstItemMarginConfig(it, true)
                }

                list.lastIndex -> {
                    firstItemMarginConfig(it, false)
                }
            }

            it.categoryTitle.text = item.title
            it.categoryConstraintLayout.setBackgroundColor(ctx.resources.getColor(item.backgroundColor))
            backgroundConfig(it, item.backgroundColor)
            holder.itemView.setOnClickListener {
                onClick.invoke(item)
                backgroundChangeAction(item)
            }
        }
    }

    private fun backgroundChangeAction(item: CategoryModel) {
        val updateList = list.map { it.copy(backgroundColor = if (item != it) R.color.white else R.color.primary_900) }
        updateList(updateList.toMutableList())
    }

    private fun firstItemMarginConfig(
        binding: RecyclerFaqCategoryItemBinding,
        isStart: Boolean
    ) {
        val params = binding.root.layoutParams as ViewGroup.MarginLayoutParams
        if (isStart)
            params.marginStart = SIXTEEN
        else
            params.marginEnd = SIXTEEN
    }

    private fun backgroundConfig(binding: RecyclerFaqCategoryItemBinding, @ColorRes color: Int) {
        with(binding) {
            when (color) {
                R.color.white -> {
                    categoryTitle.setTextColor(ctx.getColor(R.color.primary_900))
                }

                R.color.primary_900 -> {
                    categoryTitle.setTextColor(ctx.getColor(R.color.white))
                }

                else -> {
                    categoryTitle.setTextColor(ctx.getColor(R.color.black))
                }
            }
        }
    }
}