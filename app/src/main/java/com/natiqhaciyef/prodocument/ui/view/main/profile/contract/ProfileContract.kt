package com.natiqhaciyef.prodocument.ui.view.main.profile.contract

import android.content.Context
import com.natiqhaciyef.prodocument.ui.view.main.profile.model.AccountSettingModel
import com.natiqhaciyef.common.model.mapped.MappedUserWithoutPasswordModel
import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState
import com.natiqhaciyef.prodocument.ui.view.main.profile.params.preferences.model.PreferenceUIModel

object ProfileContract {

    sealed interface ProfileEvent : UiEvent {
        data object GetPaymentHistoryEvent : ProfileEvent

        data object GetSettings : ProfileEvent

        data object GetUser : ProfileEvent

        data class GetPreferences(val ctx: Context) : ProfileEvent
    }

    sealed interface ProfileEffect : UiEffect {

    }

    data class ProfileState(
        var settingList: MutableList<AccountSettingModel>? = null,
        var preferenceUIModelList: MutableList<PreferenceUIModel>? = null,
        var user: MappedUserWithoutPasswordModel? = null,
        override var isLoading: Boolean = false,
    ) : UiState
}