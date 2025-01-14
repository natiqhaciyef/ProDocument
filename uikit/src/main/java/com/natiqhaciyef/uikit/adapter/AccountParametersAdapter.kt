package com.natiqhaciyef.uikit.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.materialswitch.MaterialSwitch
import com.natiqhaciyef.core.base.ui.BaseRecyclerViewAdapter
import com.natiqhaciyef.common.model.AccountSettingModel
import com.natiqhaciyef.common.model.Settings
import com.natiqhaciyef.uikit.R
import com.natiqhaciyef.uikit.databinding.RecyclerAccountItemBinding


class AccountParametersAdapter(
    private val fragment: Fragment,
    accountList: MutableList<AccountSettingModel>,
    private val isEnabledSwitch: Boolean
) : BaseRecyclerViewAdapter<AccountSettingModel, RecyclerAccountItemBinding>(accountList) {
    override val binding: (Context, ViewGroup, Boolean) -> RecyclerAccountItemBinding =
        { ctx, vGroup, bool ->
            RecyclerAccountItemBinding.inflate(LayoutInflater.from(ctx), vGroup, bool)
        }
    private var holdIsEnabledSwitch = isEnabledSwitch

    var onClickAction: ((String) -> Unit)? = null
    var switchIconClickAction: () -> Unit = {

    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = list[position]
        val title = Settings.settingEnumToString(item.type, fragment.requireContext())
        with(holder.binding) {
            settingsIcon.setImageResource(item.image)
            settingsTitle.text = title
        }

        when (title) {
            fragment.requireContext().getString(com.natiqhaciyef.common.R.string.dark_mode) -> {
                darkModeConfig(holder.binding)
            }

            fragment.requireContext().getString(com.natiqhaciyef.common.R.string.logout) -> {
                holder.binding.settingsTitle.setTextColor(
                    ContextCompat.getColor(
                        fragment.requireContext(),
                        com.natiqhaciyef.common.R.color.gradient_start_red
                    )
                )
                logoutConfig(holder.binding)

            }

            else -> {
                holder.itemView.setOnClickListener { onClickAction?.invoke(item.type.name) }
                holder.binding.goDetailsIcon.setOnClickListener { onClickAction?.invoke(item.type.name) }
            }
        }
    }


    private fun darkModeConfig(binding: RecyclerAccountItemBinding) {
        with(binding) {
            switchIcon.visibility = View.VISIBLE
            goDetailsIcon.visibility = View.GONE
            val params = settingsTitle.layoutParams as ConstraintLayout.LayoutParams
            params.endToStart = switchIcon.id

            switchIcon.isChecked = isEnabledSwitch
            darkModeSwitchColorConfig(switchIcon)

            switchIcon.setOnClickListener {
                holdIsEnabledSwitch = !holdIsEnabledSwitch
                switchIconClickAction.invoke()
                switchIcon.isChecked = holdIsEnabledSwitch
                darkModeSwitchColorConfig(switchIcon)
            }
        }
    }

    private fun darkModeSwitchColorConfig(switchIcon: MaterialSwitch) {

        if (switchIcon.isChecked) {
            switchIcon.trackDrawable?.setColorFilter(
                ContextCompat.getColor(
                    fragment.requireContext(),
                    com.natiqhaciyef.common.R.color.primary_900
                ), PorterDuff.Mode.SRC_IN
            )

        } else {
            switchIcon.trackDrawable?.setColorFilter(
                ContextCompat.getColor(
                    fragment.requireContext(),
                    com.natiqhaciyef.common.R.color.primary_200
                ), PorterDuff.Mode.SRC_IN
            )

        }
    }

    private fun logoutConfig(binding: RecyclerAccountItemBinding) {
        with(binding) {
            goDetailsIcon.visibility = View.GONE
            val params = settingsTitle.layoutParams as ConstraintLayout.LayoutParams
            params.endToEnd = layout.id

            layout.setOnClickListener {
                // add alert dialog
            }
        }
    }
}