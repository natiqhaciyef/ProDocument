package com.natiqhaciyef.prodocument.ui.view.main.premium.contract

import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState

object PremiumContract {

    sealed class PremiumEvent : UiEvent {

    }

    sealed class PremiumEffect : UiEffect {

    }

    data class PremiumState(
        override var isLoading: Boolean = false,
    ) : UiState
}