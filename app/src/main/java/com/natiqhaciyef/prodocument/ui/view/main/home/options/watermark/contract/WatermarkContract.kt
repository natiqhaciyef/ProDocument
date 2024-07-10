package com.natiqhaciyef.prodocument.ui.view.main.home.options.watermark.contract

import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState

object WatermarkContract {

    sealed interface WatermarkEvent : UiEvent {

    }

    sealed interface WatermarkEffect : UiEffect {

    }

    data class WatermarkState(
        override var isLoading: Boolean = false,
    ) : UiState

}