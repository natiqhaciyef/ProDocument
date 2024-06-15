package com.natiqhaciyef.prodocument.ui.view.main.profile.viewmodel

import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.R
import com.natiqhaciyef.prodocument.ui.view.main.profile.model.AccountSettingModel
import com.natiqhaciyef.prodocument.ui.view.main.profile.model.Settings
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.domain.usecase.user.remote.GetUserByTokenRemoteUseCase
import com.natiqhaciyef.prodocument.ui.view.main.profile.contract.ProfileContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserByTokenRemoteUseCase: GetUserByTokenRemoteUseCase
) : BaseViewModel<ProfileContract.ProfileState, ProfileContract.ProfileEvent, ProfileContract.ProfileEffect>() {

    override fun onEventUpdate(event: ProfileContract.ProfileEvent) {
        when(event){
            is ProfileContract.ProfileEvent.GetSettings -> {
                getSettings()
            }

            is ProfileContract.ProfileEvent.GetUser -> {
                getUser()
            }

            else -> {

            }
        }
    }


    private fun getSettings() {
        val list = mutableListOf(
            AccountSettingModel(
                image = R.drawable.profile_outline_icon,
                type = Settings.PERSONAL_INFO
            ),
            AccountSettingModel(
                image = R.drawable.settings_outline_icon,
                type = Settings.PREFERENCE
            ),
            AccountSettingModel(
                image = R.drawable.security_outline_icon,
                type = Settings.SECURITY
            ),
            AccountSettingModel(
                image = R.drawable.payment_history_outline_icon,
                type = Settings.PAYMENT_HISTORY
            ),
            AccountSettingModel(
                image = R.drawable.document_outline_icon,
                type = Settings.LANGUAGE
            ),
            AccountSettingModel(
                image = R.drawable.visibility_outline_icon,
                type = Settings.DARK_MODE
            ),
            AccountSettingModel(
                image = R.drawable.paper_outline_icon,
                type = Settings.HELP_CENTER
            ),
            AccountSettingModel(
                image = R.drawable.info_outline_icon,
                type = Settings.ABOUT_PROSCAN
            ),
            AccountSettingModel(
                image = R.drawable.logout_outline_icon,
                type = Settings.LOGOUT
            )
        )

        setBaseState(getCurrentBaseState().copy(settingList = list))
    }


    private fun getUser(){
        getUserByTokenRemoteUseCase.invoke().onEach { result ->
            when(result.status){
                Status.SUCCESS -> {
                    if (result.data != null)
                        setBaseState(getCurrentBaseState().copy(isLoading = false, user = result.data))
                }

                Status.ERROR -> {
                    setBaseState(getCurrentBaseState().copy(isLoading = false))
                }

                Status.LOADING -> {
                    setBaseState(getCurrentBaseState().copy(isLoading = true))
                }
            }
        }.launchIn(viewModelScope)
    }


    override fun getInitialState(): ProfileContract.ProfileState = ProfileContract.ProfileState()
}