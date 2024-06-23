package com.natiqhaciyef.prodocument.ui.view.main.profile.contract

import android.content.Context
import com.natiqhaciyef.common.model.FaqModel
import com.natiqhaciyef.common.model.LanguageModel
import com.natiqhaciyef.common.model.ProScanInfoModel
import com.natiqhaciyef.common.model.mapped.MappedSubscriptionModel
import com.natiqhaciyef.prodocument.ui.view.main.profile.model.AccountSettingModel
import com.natiqhaciyef.common.model.mapped.MappedUserWithoutPasswordModel
import com.natiqhaciyef.common.model.payment.PaymentHistoryModel
import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState
import com.natiqhaciyef.prodocument.ui.view.main.profile.params.model.ParamsUIModel

object ProfileContract {

    sealed interface ProfileEvent : UiEvent {
        data object GetPaymentHistory: ProfileEvent

        data object GetSettings : ProfileEvent

        data object GetAccountInfo : ProfileEvent

        data object GetFaqList : ProfileEvent

        data object GetProscanInfo : ProfileEvent

        data class GetAllSupportedLanguages(val context: Context) : ProfileEvent

        data class GetSubscriptionInfo(val user: MappedUserWithoutPasswordModel) : ProfileEvent

        data class GetPreferences(val ctx: Context) : ProfileEvent

        data class GetSecurityParams(val ctx: Context) : ProfileEvent
    }

    sealed interface ProfileEffect : UiEffect {

    }

    data class ProfileState(
        var settingList: MutableList<AccountSettingModel>? = null,
        var paramsUIModelList: MutableList<ParamsUIModel>? = null,
        var user: MappedUserWithoutPasswordModel? = null,
        var languages: List<LanguageModel>? = null,
        var pickedPlan: MappedSubscriptionModel? = null,
        var paymentsHistory: List<PaymentHistoryModel>? = null,
        var faqList: List<FaqModel>? = null,
        var proscanDetails: ProScanInfoModel? = null,
        override var isLoading: Boolean = false,
    ) : UiState
}