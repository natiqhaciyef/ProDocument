package com.natiqhaciyef.prodocument.ui.view.main.profile.params.preferences.adapter

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.natiqhaciyef.core.base.ui.BaseRecyclerViewAdapter
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.RecyclerParamsItemBinding
import com.natiqhaciyef.prodocument.ui.view.main.profile.model.FieldType
import com.natiqhaciyef.prodocument.ui.view.main.profile.model.ParamsUIModel


class ParamsAdapter(
    private val ctx: Context,
    private val paramsList: MutableList<ParamsUIModel>,
    @ColorRes
    private val backgroundColor: Int = com.natiqhaciyef.common.R.color.grayscale_50
) : BaseRecyclerViewAdapter<ParamsUIModel, RecyclerParamsItemBinding>(list = paramsList) {
    private var isChecked: Boolean = false

    var action: (ParamsUIModel) -> Unit = {}

    override val binding: (Context, ViewGroup, Boolean) -> RecyclerParamsItemBinding =
        { ctx, vg, bool ->
            RecyclerParamsItemBinding.inflate(LayoutInflater.from(ctx), vg, bool)
        }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = list[position]
        if (item.fieldIcon != null)
            iconImportConfig(holder.binding, item)

        holder.binding.preferenceLayout.setBackgroundColor(ctx.getColor(backgroundColor))

        when (item.fieldType) {
            FieldType.SPACE -> {
                holder.itemView.setOnClickListener { action.invoke(item) }
                spaceConfig(holder.binding)
            }

            FieldType.LINE -> { lineConfig(holder.binding) }

            FieldType.TITLE -> { titleConfig(item, holder.binding) }

            FieldType.NAVIGATION -> {
                holder.itemView.setOnClickListener { action.invoke(item) }
                navigationConfig(item, holder.binding, action)
            }

            FieldType.SWITCH -> { switchConfig(item, holder.binding, action)  }

            else -> {}
        }
    }

    private fun iconImportConfig(binding: RecyclerParamsItemBinding, item: ParamsUIModel){
        with(binding) {
            if (preferenceTitle.visibility == View.VISIBLE) {
                prefixIcon.visibility = View.VISIBLE
                prefixIcon.setImageResource(item.fieldIcon!!)
            }
        }
    }

    private fun lineConfig(binding: RecyclerParamsItemBinding){
        with(binding){
            switchIcon.visibility = View.GONE
            goDetailsIcon.visibility = View.GONE
            preferenceLine.visibility = View.VISIBLE
            preferenceTitle.visibility = View.GONE
        }
    }

    private fun spaceConfig(binding: RecyclerParamsItemBinding){
        with(binding){
            switchIcon.visibility = View.INVISIBLE
            goDetailsIcon.visibility = View.GONE
            preferenceLine.visibility = View.INVISIBLE
            preferenceTitle.visibility = View.INVISIBLE
        }
    }

    private fun titleConfig(
        item: ParamsUIModel,
        binding: RecyclerParamsItemBinding
    ) {
        with(binding) {
            switchIcon.visibility = View.GONE
            goDetailsIcon.visibility = View.GONE
            preferenceLine.visibility = View.GONE
            val params = preferenceTitle.layoutParams as ConstraintLayout.LayoutParams
            params.endToEnd = preferenceLayout.id
            params.topToBottom = preferenceLine.id

            preferenceTitle.text = item.title
            preferenceTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
            preferenceTitle.setTextColor(ContextCompat.getColor(ctx, com.natiqhaciyef.common.R.color.grayscale_700))
        }
    }

    private fun switchConfig(
        item: ParamsUIModel,
        binding: RecyclerParamsItemBinding,
        action: (ParamsUIModel) -> Unit = {}
    ) {
        with(binding) {
            switchIcon.visibility = View.VISIBLE
            preferenceLine.visibility = View.GONE
            goDetailsIcon.visibility = View.GONE

            val params = preferenceTitle.layoutParams as ConstraintLayout.LayoutParams
            params.endToStart = switchIcon.id
            params.topToTop = preferenceLayout.id

            preferenceTitle.text = item.title
            preferenceTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
            preferenceTitle.setTextColor(ContextCompat.getColor(ctx, com.natiqhaciyef.common.R.color.grayscale_900))
            switchIcon.setOnCheckedChangeListener { compoundButton, b ->
                isChecked = !isChecked
                action.invoke(item)
            }
        }
    }

    private fun navigationConfig(
        item: ParamsUIModel,
        binding: RecyclerParamsItemBinding,
        action: (ParamsUIModel) -> Unit = {}
    ) {
        with(binding) {
            goDetailsIcon.visibility = View.VISIBLE
            switchIcon.visibility = View.GONE
            preferenceLine.visibility = View.GONE

            val params = preferenceTitle.layoutParams as ConstraintLayout.LayoutParams
            params.endToStart = goDetailsIcon.id
            params.topToTop = preferenceLayout.id

            preferenceTitle.text = item.title
            preferenceTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
            preferenceTitle.setTextColor(ContextCompat.getColor(ctx, com.natiqhaciyef.common.R.color.grayscale_900))
            goDetailsIcon.setOnClickListener { action.invoke(item) }
        }
    }
}