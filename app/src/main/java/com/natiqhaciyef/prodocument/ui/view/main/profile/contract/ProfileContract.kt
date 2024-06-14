package com.natiqhaciyef.prodocument.ui.view.main.profile.contract

import android.content.Context
import com.natiqhaciyef.common.model.AccountSettingModel
import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState

object ProfileContract {

    sealed interface ProfileEvent : UiEvent {
//        data object GetPaymentHistoryEvent: ProfileEvent
        data class GetSettingsEvent(val ctx: Context): ProfileEvent
    }

    sealed interface ProfileEffect : UiEffect {

    }

    data class ProfileState(
        var settingList: MutableList<AccountSettingModel>? = null,
        override var isLoading: Boolean = false,
    ) : UiState
}