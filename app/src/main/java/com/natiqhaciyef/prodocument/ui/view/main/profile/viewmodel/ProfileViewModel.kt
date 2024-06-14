package com.natiqhaciyef.prodocument.ui.view.main.profile.viewmodel

import android.content.Context
import com.natiqhaciyef.common.model.AccountSettingModel
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.ui.view.main.profile.contract.ProfileContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(

) : BaseViewModel<ProfileContract.ProfileState, ProfileContract.ProfileEvent, ProfileContract.ProfileEffect>() {

    override fun onEventUpdate(event: ProfileContract.ProfileEvent) {
        when(event){
            is ProfileContract.ProfileEvent.GetSettingsEvent -> {
                getSettings(event.ctx)
            }

            else -> {

            }
        }
    }


    private fun getSettings(context: Context) {
        val list = mutableListOf(
            AccountSettingModel(
                image = com.natiqhaciyef.common.R.drawable.profile_outline_icon,
                title = context.getString(com.natiqhaciyef.common.R.string.personal_info)
            ),
            AccountSettingModel(
                image = com.natiqhaciyef.common.R.drawable.settings_outline_icon,
                title = context.getString(com.natiqhaciyef.common.R.string.preferences)
            ),
            AccountSettingModel(
                image = com.natiqhaciyef.common.R.drawable.security_outline_icon,
                title = context.getString(com.natiqhaciyef.common.R.string.security)
            ),
            AccountSettingModel(
                image = com.natiqhaciyef.common.R.drawable.payment_history_outline_icon,
                title = context.getString(com.natiqhaciyef.common.R.string.payment_history)
            ),
            AccountSettingModel(
                image = com.natiqhaciyef.common.R.drawable.document_outline_icon,
                title = context.getString(com.natiqhaciyef.common.R.string.language)
            ),
            AccountSettingModel(
                image = com.natiqhaciyef.common.R.drawable.visibility_outline_icon,
                title = context.getString(com.natiqhaciyef.common.R.string.dark_mode)
            ),
            AccountSettingModel(
                image = com.natiqhaciyef.common.R.drawable.paper_outline_icon,
                title = context.getString(com.natiqhaciyef.common.R.string.help_center)
            ),
            AccountSettingModel(
                image = com.natiqhaciyef.common.R.drawable.info_outline_icon,
                title = context.getString(com.natiqhaciyef.common.R.string.about_proscan)
            ),
            AccountSettingModel(
                image = com.natiqhaciyef.common.R.drawable.logout_outline_icon,
                title = context.getString(com.natiqhaciyef.common.R.string.logout)
            )
        )

        setBaseState(getCurrentBaseState().copy(settingList = list))
    }


    override fun getInitialState(): ProfileContract.ProfileState = ProfileContract.ProfileState()
}