package com.natiqhaciyef.prodocument.ui.view.main.profile.params.security.adapter

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.natiqhaciyef.core.base.ui.BaseRecyclerViewAdapter
import com.natiqhaciyef.common.R
import com.natiqhaciyef.prodocument.databinding.RecyclerParamsItemBinding
import com.natiqhaciyef.prodocument.ui.manager.RememberUserManager
import com.natiqhaciyef.prodocument.ui.view.main.profile.params.model.FieldType
import com.natiqhaciyef.prodocument.ui.view.main.profile.params.model.ParamsUIModel

class SecurityParamsAdapter(
    private val ctx: Context,
    securityParamsList: MutableList<ParamsUIModel>,
) : BaseRecyclerViewAdapter<ParamsUIModel, RecyclerParamsItemBinding>(securityParamsList) {
    private var isChecked = false
    override val binding: (Context, ViewGroup, Boolean) -> RecyclerParamsItemBinding =
        { ctx, vg, bool ->
            RecyclerParamsItemBinding.inflate(LayoutInflater.from(ctx), vg, bool)
        }
    var onClickAction: (ParamsUIModel) -> Unit = {

    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = list[position]

        holder.binding.let {
            when (item.fieldType) {
                FieldType.SWITCH -> {
                    switchConfig(item, it)
                }

                FieldType.NAVIGATION -> {
                    navigationConfig(item, it)
                }

                else -> {}
            }
        }

        holder.itemView.setOnClickListener {
            securityProcessConfiguration(item)
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
            preferenceTitle.setTextColor(ContextCompat.getColor(ctx, R.color.grayscale_900))
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
            preferenceTitle.setTextColor(ContextCompat.getColor(ctx, R.color.grayscale_900))
            goDetailsIcon.setOnClickListener { action.invoke() }
        }
    }

    private fun securityProcessConfiguration(paramsUIModel: ParamsUIModel) = when (paramsUIModel.title) {
        ctx.getString(R.string.remember_me_param) -> { RememberUserManager.rememberState(ctx, isChecked) }

        ctx.getString(R.string.biometric_id) -> { }

        ctx.getString(R.string.face_id) -> { }

        ctx.getString(R.string.sms_authenticator) -> { }

        ctx.getString(R.string.google_authenticator) -> { }

        ctx.getString(R.string.device_management) -> { }

        else -> {}
    }
}