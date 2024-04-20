package com.natiqhaciyef.prodocument.ui.view.main.profile.contract

import com.natiqhaciyef.prodocument.ui.base.UiEffect
import com.natiqhaciyef.prodocument.ui.base.UiEvent
import com.natiqhaciyef.prodocument.ui.base.UiState

object ProfileContract {

    sealed class ProfileEvent : UiEvent {

    }

    sealed class ProfileEffect : UiEffect {

    }

    data class ProfileState(
        override var isLoading: Boolean = false,
    ) : UiState
}