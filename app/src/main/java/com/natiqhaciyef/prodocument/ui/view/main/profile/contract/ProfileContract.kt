package com.natiqhaciyef.prodocument.ui.view.main.profile.contract

import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState

object ProfileContract {

    sealed class ProfileEvent : UiEvent {

    }

    sealed class ProfileEffect : UiEffect {

    }

    data class ProfileState(
        override var isLoading: Boolean = false,
    ) : UiState
}