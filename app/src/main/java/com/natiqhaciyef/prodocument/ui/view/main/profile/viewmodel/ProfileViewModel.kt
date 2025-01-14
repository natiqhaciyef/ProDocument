package com.natiqhaciyef.prodocument.ui.view.main.profile.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.model.CategoryModel
import com.natiqhaciyef.common.model.ContactMethods
import com.natiqhaciyef.common.model.LanguageModel
import com.natiqhaciyef.common.model.QuestionCategories
import com.natiqhaciyef.common.model.AccountSettingModel
import com.natiqhaciyef.common.model.Settings
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.model.mapped.MappedUserWithoutPasswordModel
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.domain.usecase.app.GetCountriesUseCase
import com.natiqhaciyef.domain.usecase.app.GetFaqListUseCase
import com.natiqhaciyef.domain.usecase.app.GetProscanDetailsUseCase
import com.natiqhaciyef.domain.usecase.app.GetProscanSectionsUseCase
import com.natiqhaciyef.domain.usecase.payment.GetPaymentHistoryUseCase
import com.natiqhaciyef.domain.usecase.subscription.GetPickedPlanUseCase
import com.natiqhaciyef.domain.usecase.user.GetUserByTokenRemoteUseCase
import com.natiqhaciyef.domain.usecase.user.GetUserStaticsUseCase
import com.natiqhaciyef.prodocument.ui.view.main.profile.contract.ProfileContract
import com.natiqhaciyef.common.model.FieldType
import com.natiqhaciyef.common.model.ParamsUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserByTokenRemoteUseCase: GetUserByTokenRemoteUseCase,
    private val getPickedPlanUseCase: GetPickedPlanUseCase,
    private val getPaymentHistoryUseCase: GetPaymentHistoryUseCase,
    private val getFaqListUseCase: GetFaqListUseCase,
    private val getProscanDetailsUseCase: GetProscanDetailsUseCase,
    private val getProscanSectionsUseCase: GetProscanSectionsUseCase,
    private val getCountriesUseCase: GetCountriesUseCase,
    private val getUserStatistics: GetUserStaticsUseCase
) : BaseViewModel<ProfileContract.ProfileState, ProfileContract.ProfileEvent, ProfileContract.ProfileEffect>() {

    override fun onEventUpdate(event: ProfileContract.ProfileEvent) {
        when(event){
            is ProfileContract.ProfileEvent.GetCountries -> getAllCountries()

            is ProfileContract.ProfileEvent.GetSettings -> getSettings()

            is ProfileContract.ProfileEvent.GetUserStatistics -> getUserStatics()

            is ProfileContract.ProfileEvent.GetAccountInfo -> getAccount()

            is ProfileContract.ProfileEvent.GetSubscriptionInfo -> getPickedPlan(event.user)

            is ProfileContract.ProfileEvent.GetPaymentHistory -> getPaymentHistory()

            is ProfileContract.ProfileEvent.GetPreferences -> getPreferences(event.ctx)

            is ProfileContract.ProfileEvent.GetAllSupportedLanguages -> getAllLanguages(event.context)

            is ProfileContract.ProfileEvent.GetSecurityParams -> getSecurityParams(event.ctx)

            is ProfileContract.ProfileEvent.GetFaqList -> getFaqList()

            is ProfileContract.ProfileEvent.GetProscanInfo -> getProscanDetails()

            is ProfileContract.ProfileEvent.GetProscanSections -> getProscanSections()

            is ProfileContract.ProfileEvent.GetFaqCategories -> getAllFaqCategories()

            is ProfileContract.ProfileEvent.GetContactMethods -> getContactMethods()

            is ProfileContract.ProfileEvent.ClearState -> clearState()
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

    private fun getUserStatics(){
        getUserStatistics.invoke().onEach { result ->
            when(result.status){
                Status.SUCCESS -> {
                    setBaseState(getCurrentBaseState().copy(userStatistics = result.data, isLoading = false))
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

    private fun getAllCountries(){
        getCountriesUseCase.invoke().onEach { result ->
            when(result.status){
                Status.SUCCESS -> {
                    if (result.data != null)
                        setBaseState(getHoldState().copy(countries = result.data, isLoading = false))
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

    private fun getProscanDetails(){
        getProscanDetailsUseCase.invoke().onEach { result ->
            when(result.status){
                Status.SUCCESS -> {
                    if (result.data != null)
                        setBaseState(getCurrentBaseState().copy(proscanInfo = result.data, isLoading = false))
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

    private fun getProscanSections(){
        getProscanSectionsUseCase.invoke().onEach { result ->
            when(result.status) {
                Status.SUCCESS -> {
                    if (result.data != null)
                        setBaseState(getHoldState().copy(proscanSections = result.data, isLoading = false))
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

    private fun getFaqList(){
        getFaqListUseCase.invoke().onEach { result ->
            when(result.status){
                Status.SUCCESS -> {
                    if (result.data != null)
                        setBaseState(getCurrentBaseState().copy(faqList = result.data, isLoading = false))
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
                image = R.drawable.graph_outline_icon,
                type = Settings.CATEGORY_GRAPH
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
            ParamsUIModel(title = ctx.getString(R.string.scan), FieldType.TITLE),
            ParamsUIModel(title = ctx.getString(R.string.high_quality_scan), FieldType.SWITCH),
            ParamsUIModel(title = ctx.getString(R.string.auto_crop_image), FieldType.SWITCH),
            ParamsUIModel(title = ctx.getString(R.string.enhance_mode), FieldType.NAVIGATION),

            ParamsUIModel(title = EMPTY_STRING, FieldType.LINE),
            ParamsUIModel(title = ctx.getString(R.string.file_naming), FieldType.TITLE),
            ParamsUIModel(title = ctx.getString(R.string.default_file_name), FieldType.NAVIGATION),

            ParamsUIModel(title = EMPTY_STRING, FieldType.LINE),
            ParamsUIModel(title = ctx.getString(R.string.files_storage), FieldType.TITLE),
            ParamsUIModel(title = ctx.getString(R.string.save_original_images_to_gallery), FieldType.SWITCH),
            ParamsUIModel(title = ctx.getString(R.string.free_up_storage), FieldType.NAVIGATION),

            ParamsUIModel(title = EMPTY_STRING, FieldType.LINE),
            ParamsUIModel(title = ctx.getString(R.string.payment_subscriptions), FieldType.TITLE),
            ParamsUIModel(title = ctx.getString(R.string.subscription_management), FieldType.NAVIGATION),
            ParamsUIModel(title = ctx.getString(R.string.manage_payment_methods), FieldType.NAVIGATION),

            ParamsUIModel(title = EMPTY_STRING, FieldType.LINE),
            ParamsUIModel(title = ctx.getString(R.string.cloud_n_sync), FieldType.TITLE),
            ParamsUIModel(title = ctx.getString(R.string.cloud_sync), FieldType.NAVIGATION),
            ParamsUIModel(title = ctx.getString(R.string.local_folder_sync), FieldType.NAVIGATION),
            ParamsUIModel(title = EMPTY_STRING, FieldType.SPACE)
        )

        setBaseState(getCurrentBaseState().copy(paramsUIModelList = list))
    }

    private fun getSecurityParams(ctx: Context){
        val list = mutableListOf(
            ParamsUIModel(title = ctx.getString(R.string.remember_me_param), FieldType.SWITCH),
            ParamsUIModel(title = ctx.getString(R.string.biometric_id), FieldType.SWITCH),
//            ParamsUIModel(title = ctx.getString(R.string.face_id), FieldType.SWITCH),
            ParamsUIModel(title = ctx.getString(R.string.sms_authenticator), FieldType.SWITCH, false),
            ParamsUIModel(title = ctx.getString(R.string.google_authenticator), FieldType.SWITCH, false),
            ParamsUIModel(title = ctx.getString(R.string.device_management), FieldType.NAVIGATION),
        )

        setBaseState(getCurrentBaseState().copy(paramsUIModelList = list))
    }

    private fun getAllLanguages(ctx: Context){
        val languages = listOf(
            LanguageModel(title = ctx.getString(R.string.az_tag), ctx.getString(R.string.azerbaijani), false),
            LanguageModel(title = ctx.getString(R.string.tr_tag), ctx.getString(R.string.turkish), false),
            LanguageModel(title = EMPTY_STRING, ctx.getString(R.string.english), false),
        )

        setBaseState(getCurrentBaseState().copy(languages = languages))
    }

    private fun getAllFaqCategories(){
        val categoriesTitle = QuestionCategories.makeQuestionTypeStringList()
        val mappedList = categoriesTitle
            .map {
                CategoryModel(
                    title = it,
                    backgroundColor = if (categoriesTitle.first() == it) R.color.primary_900 else R.color.white
                )
            }

        setBaseState(getCurrentBaseState().copy(faqCategoryList = mappedList))
    }

    private fun getContactMethods(){
        setBaseState(getCurrentBaseState().copy(contactMethods = ContactMethods.makeContactList()))
    }

    private fun clearState(){
        setBaseState(getCurrentBaseState())
    }

    override fun getInitialState(): ProfileContract.ProfileState = ProfileContract.ProfileState()
}