package com.natiqhaciyef.prodocument.ui.view.main.home.options.split.contract

import com.natiqhaciyef.prodocument.ui.base.UiEffect
import com.natiqhaciyef.prodocument.ui.base.UiEvent
import com.natiqhaciyef.prodocument.ui.base.UiState

object SplitContract {

    sealed class SplitEvent : UiEvent {

    }

    sealed class SplitEffect : UiEffect {

    }

    data class SplitState(
        override var isLoading: Boolean = false,
    ) : UiState

}