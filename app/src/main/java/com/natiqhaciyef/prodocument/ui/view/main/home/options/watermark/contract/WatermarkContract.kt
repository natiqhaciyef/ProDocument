package com.natiqhaciyef.prodocument.ui.view.main.home.options.watermark.contract

import com.natiqhaciyef.prodocument.ui.base.UiEffect
import com.natiqhaciyef.prodocument.ui.base.UiEvent
import com.natiqhaciyef.prodocument.ui.base.UiState

object WatermarkContract {

    sealed class WatermarkEvent : UiEvent {

    }

    sealed class WatermarkEffect : UiEffect {

    }

    data class WatermarkState(
        override var isLoading: Boolean = false,
    ) : UiState

}