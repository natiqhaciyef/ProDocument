package com.natiqhaciyef.prodocument.ui.view.main.profile.params.helpcenter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.natiqhaciyef.common.constants.SIXTEEN
import com.natiqhaciyef.common.constants.TWELVE
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.common.model.CategoryModel
import com.natiqhaciyef.core.base.ui.BaseRecyclerViewAdapter
import com.natiqhaciyef.prodocument.databinding.RecyclerFaqCategoryItemBinding

class CategoryAdapter(
    private val ctx: Context,
    categoryList: MutableList<CategoryModel>
) : BaseRecyclerViewAdapter<CategoryModel, RecyclerFaqCategoryItemBinding>(categoryList) {
    override val binding: (Context, ViewGroup, Boolean) -> RecyclerFaqCategoryItemBinding
        get() = { ctx, vg, bool ->
            RecyclerFaqCategoryItemBinding.inflate(LayoutInflater.from(ctx), vg, bool)
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
        }
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
                com.natiqhaciyef.common.R.color.white -> {
                    categoryTitle.setTextColor(ctx.getColor(com.natiqhaciyef.common.R.color.primary_900))
                }

                com.natiqhaciyef.common.R.color.primary_900 -> {
                    categoryTitle.setTextColor(ctx.getColor(com.natiqhaciyef.common.R.color.white))
                }

                else -> {
                    categoryTitle.setTextColor(ctx.getColor(com.natiqhaciyef.common.R.color.black))
                }
            }
        }
    }
}