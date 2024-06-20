package com.natiqhaciyef.prodocument.ui.view.main.profile.params.preferences.adapter

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.natiqhaciyef.core.base.ui.BaseRecyclerViewAdapter
import com.natiqhaciyef.prodocument.databinding.RecyclerParamsItemBinding
import com.natiqhaciyef.prodocument.ui.view.main.profile.params.model.FieldType
import com.natiqhaciyef.prodocument.ui.view.main.profile.params.model.ParamsUIModel


class PreferencesAdapter(
    private val ctx: Context,
    private val preferenceList: MutableList<ParamsUIModel>
) :
    BaseRecyclerViewAdapter<ParamsUIModel, RecyclerParamsItemBinding>(list = preferenceList) {
    private var isChecked: Boolean = false

    override val binding: (Context, ViewGroup, Boolean) -> RecyclerParamsItemBinding =
        { ctx, vg, bool ->
            RecyclerParamsItemBinding.inflate(LayoutInflater.from(ctx), vg, bool)
        }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = list[position]
        when (item.fieldType) {
            FieldType.SPACE -> { spaceConfig(holder.binding) }

            FieldType.LINE -> { lineConfig(holder.binding) }

            FieldType.TITLE -> { titleConfig(item, holder.binding) }


            FieldType.NAVIGATION -> { navigationConfig(item, holder.binding) {} }

            FieldType.SWITCH -> { switchConfig(item, holder.binding) {} }

            else -> {}
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
        action: () -> Unit = {}
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
                action.invoke()
            }
        }
    }

    private fun navigationConfig(
        item: ParamsUIModel,
        binding: RecyclerParamsItemBinding,
        action: () -> Unit = {}
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
            goDetailsIcon.setOnClickListener {
                action.invoke()
            }
        }
    }
}