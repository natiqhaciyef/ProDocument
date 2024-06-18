package com.natiqhaciyef.prodocument.ui.view.main.profile.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.model.LanguageModel
import com.natiqhaciyef.prodocument.ui.view.main.profile.model.AccountSettingModel
import com.natiqhaciyef.prodocument.ui.view.main.profile.model.Settings
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.model.mapped.MappedUserWithoutPasswordModel
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.domain.usecase.payment.remote.GetPaymentHistoryUseCase
import com.natiqhaciyef.domain.usecase.subscription.GetPickedPlanUseCase
import com.natiqhaciyef.domain.usecase.user.remote.GetUserByTokenRemoteUseCase
import com.natiqhaciyef.prodocument.ui.view.main.profile.contract.ProfileContract
import com.natiqhaciyef.prodocument.ui.view.main.profile.params.preferences.model.FieldType
import com.natiqhaciyef.prodocument.ui.view.main.profile.params.preferences.model.PreferenceUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserByTokenRemoteUseCase: GetUserByTokenRemoteUseCase,
    private val getPickedPlanUseCase: GetPickedPlanUseCase,
    private val getPaymentHistoryUseCase: GetPaymentHistoryUseCase
) : BaseViewModel<ProfileContract.ProfileState, ProfileContract.ProfileEvent, ProfileContract.ProfileEffect>() {

    override fun onEventUpdate(event: ProfileContract.ProfileEvent) {
        when(event){
            is ProfileContract.ProfileEvent.GetSettings -> {
                getSettings()
            }

            is ProfileContract.ProfileEvent.GetAccountInfo -> {
                getAccount()
            }

            is ProfileContract.ProfileEvent.GetSubscriptionInfo -> {
                getPickedPlan(event.user)
            }

            is ProfileContract.ProfileEvent.GetPaymentHistory -> {
                getPaymentHistory()
            }

            is ProfileContract.ProfileEvent.GetPreferences -> {
                getPreferences(event.ctx)
            }

            is ProfileContract.ProfileEvent.GetAllSupportedLanguages -> {
                getAllLanguages(event.context)
            }
        }
    }


    private fun getPaymentHistory(){
        getPaymentHistoryUseCase.invoke().onEach { result ->
            when(result.status){
                Status.SUCCESS -> {
                    if(result.data != null)
                        setBaseState(getCurrentBaseState().copy(isLoading = false, paymentsHistory = result.data))
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

    private fun getPickedPlan(user: MappedUserWithoutPasswordModel){
        getPickedPlanUseCase.operate(user.email).onEach { result ->
            when(result.status){
                Status.SUCCESS -> {
                    if (result.data != null)
                        setBaseState(getCurrentBaseState().copy(isLoading = false, pickedPlan = result.data, user = user))
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

    private fun getAccount(){
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

    private fun getPreferences(ctx: Context){
        val list = mutableListOf(
            PreferenceUIModel(title = ctx.getString(R.string.scan), FieldType.TITLE),
            PreferenceUIModel(title = ctx.getString(R.string.high_quality_scan), FieldType.SWITCH),
            PreferenceUIModel(title = ctx.getString(R.string.auto_crop_image), FieldType.SWITCH),
            PreferenceUIModel(title = ctx.getString(R.string.enhance_mode), FieldType.NAVIGATION),

            PreferenceUIModel(title = "", FieldType.LINE),
            PreferenceUIModel(title = ctx.getString(R.string.file_naming), FieldType.TITLE),
            PreferenceUIModel(title = ctx.getString(R.string.default_file_name), FieldType.NAVIGATION),

            PreferenceUIModel(title = "", FieldType.LINE),
            PreferenceUIModel(title = ctx.getString(R.string.files_storage), FieldType.TITLE),
            PreferenceUIModel(title = ctx.getString(R.string.save_original_images_to_gallery), FieldType.SWITCH),
            PreferenceUIModel(title = ctx.getString(R.string.free_up_storage), FieldType.NAVIGATION),

            PreferenceUIModel(title = "", FieldType.LINE),
            PreferenceUIModel(title = ctx.getString(R.string.payment_subscriptions), FieldType.TITLE),
            PreferenceUIModel(title = ctx.getString(R.string.subscription_management), FieldType.NAVIGATION),
            PreferenceUIModel(title = ctx.getString(R.string.manage_payment_methods), FieldType.NAVIGATION),

            PreferenceUIModel(title = "", FieldType.LINE),
            PreferenceUIModel(title = ctx.getString(R.string.cloud_n_sync), FieldType.TITLE),
            PreferenceUIModel(title = ctx.getString(R.string.cloud_sync), FieldType.NAVIGATION),
            PreferenceUIModel(title = ctx.getString(R.string.local_folder_sync), FieldType.NAVIGATION),
            PreferenceUIModel(title = "", FieldType.SPACE)
        )

        setBaseState(getCurrentBaseState().copy(preferenceUIModelList = list))
    }

    private fun getAllLanguages(ctx: Context){
        val languages = listOf(
            LanguageModel(title = ctx.getString(R.string.az_tag), ctx.getString(R.string.azerbaijani), false),
            LanguageModel(title = ctx.getString(R.string.tr_tag), ctx.getString(R.string.turkish), false),
            LanguageModel(title = "", ctx.getString(R.string.english), false),
        )

        setBaseState(getCurrentBaseState().copy(languages = languages))
    }

    override fun getInitialState(): ProfileContract.ProfileState = ProfileContract.ProfileState()
}