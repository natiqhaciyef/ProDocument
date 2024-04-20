package com.natiqhaciyef.prodocument.ui.view.main.premium.contract

import com.natiqhaciyef.prodocument.ui.base.UiEffect
import com.natiqhaciyef.prodocument.ui.base.UiEvent
import com.natiqhaciyef.prodocument.ui.base.UiState

object PremiumContract {

    sealed class PremiumEvent : UiEvent {

    }

    sealed class PremiumEffect : UiEffect {

    }

    data class PremiumState(
        override var isLoading: Boolean = false,
    ) : UiState
}