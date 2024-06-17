package com.natiqhaciyef.prodocument.ui.view.main.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.natiqhaciyef.prodocument.ui.view.main.profile.model.AccountSettingModel
import com.natiqhaciyef.prodocument.ui.view.main.profile.model.Settings
import com.natiqhaciyef.core.base.ui.BaseRecyclerViewAdapter
import com.natiqhaciyef.prodocument.databinding.RecyclerAccountItemBinding


class AccountParametersAdapter(
    private val fragment: Fragment,
    accountList: MutableList<AccountSettingModel>
): BaseRecyclerViewAdapter<AccountSettingModel, RecyclerAccountItemBinding>(accountList) {
    override val binding: (Context, ViewGroup, Boolean) -> RecyclerAccountItemBinding = { ctx, vGroup, bool ->
        RecyclerAccountItemBinding.inflate(LayoutInflater.from(ctx), vGroup, bool)
    }
    private var isEnabledSwitch = false

    var onClickAction: ((String) -> Unit)? = null

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = list[position]
        val title = Settings.settingEnumToString(item.type, fragment.requireContext())
        with(holder.binding){
            settingsIcon.setImageResource(item.image)
            settingsTitle.text = title
        }

        when(title){
            fragment.requireContext().getString(com.natiqhaciyef.common.R.string.dark_mode) -> {
                darkModeConfig(holder.binding)
            }

            fragment.requireContext().getString(com.natiqhaciyef.common.R.string.logout) -> {
                holder.binding.settingsTitle.setTextColor(ContextCompat.getColor(fragment.requireContext(), com.natiqhaciyef.common.R.color.gradient_start_red))
                logoutConfig(holder.binding)
            }

            else -> {
                holder.binding.goDetailsIcon.setOnClickListener { onClickAction?.invoke(item.type.name) }
                holder.itemView.setOnClickListener { onClickAction?.invoke(item.type.name) }
            }
        }
    }


    private fun darkModeConfig(binding: RecyclerAccountItemBinding){
        with(binding){
            switchIcon.visibility = View.VISIBLE
            goDetailsIcon.visibility = View.GONE
            val params = settingsTitle.layoutParams as ConstraintLayout.LayoutParams
            params.endToStart = switchIcon.id

            switchIcon.setOnClickListener {
                isEnabledSwitch = !isEnabledSwitch
                switchIcon.isChecked = isEnabledSwitch
            }
        }
    }

    private fun logoutConfig(binding: RecyclerAccountItemBinding){
        with(binding){
            goDetailsIcon.visibility = View.GONE
            val params = settingsTitle.layoutParams as ConstraintLayout.LayoutParams
            params.endToEnd = layout.id

            layout.setOnClickListener {
                // add alert dialog
            }
        }
    }
}