package com.natiqhaciyef.prodocument.ui.view.main.profile.contract

import android.content.Context
import com.natiqhaciyef.common.model.CategoryModel
import com.natiqhaciyef.common.model.ContactMethods
import com.natiqhaciyef.common.model.FaqModel
import com.natiqhaciyef.common.model.LanguageModel
import com.natiqhaciyef.common.model.MappedGraphDetailModel
import com.natiqhaciyef.common.model.ProScanInfoModel
import com.natiqhaciyef.common.model.ProscanSectionModel
import com.natiqhaciyef.common.model.mapped.MappedSubscriptionModel
import com.natiqhaciyef.common.model.AccountSettingModel
import com.natiqhaciyef.common.model.mapped.MappedUserWithoutPasswordModel
import com.natiqhaciyef.common.model.payment.PaymentHistoryModel
import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState
import com.natiqhaciyef.common.model.ParamsUIModel

object ProfileContract {

    sealed interface ProfileEvent : UiEvent {
        data object GetPaymentHistory: ProfileEvent

        data object GetSettings : ProfileEvent

        data object GetAccountInfo : ProfileEvent

        data object GetCountries : ProfileEvent

        data object GetUserStatistics : ProfileEvent

        data object GetFaqList : ProfileEvent

        data object GetFaqCategories : ProfileEvent

        data object GetProscanInfo : ProfileEvent

        data object GetProscanSections : ProfileEvent

        data object GetContactMethods : ProfileEvent

        data class GetAllSupportedLanguages(val context: Context) : ProfileEvent

        data class GetSubscriptionInfo(val user: MappedUserWithoutPasswordModel) : ProfileEvent

        data class GetPreferences(val ctx: Context) : ProfileEvent

        data class GetSecurityParams(val ctx: Context) : ProfileEvent

        data object ClearState : ProfileEvent
    }

    sealed interface ProfileEffect : UiEffect {

    }

    data class ProfileState(
        var settingList: MutableList<AccountSettingModel>? = null,
        var paramsUIModelList: MutableList<ParamsUIModel>? = null,
        var user: MappedUserWithoutPasswordModel? = null,
        var userStatistics: List<MappedGraphDetailModel>? = null,
        var countries: List<String>? = null,
        var languages: List<LanguageModel>? = null,
        var pickedPlan: MappedSubscriptionModel? = null,
        var paymentsHistory: List<PaymentHistoryModel>? = null,
        var faqList: List<FaqModel>? = null,
        var proscanInfo: ProScanInfoModel? = null,
        var proscanSections: List<ProscanSectionModel>? = null,
        var faqCategoryList: List<CategoryModel>? = null,
        var contactMethods: List<ContactMethods>? = null,
        override var isLoading: Boolean = false,
    ) : UiState
}