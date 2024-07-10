package com.natiqhaciyef.prodocument.ui.view.main.home.options.split.contract

import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState

object SplitContract {

    sealed interface SplitEvent : UiEvent {
        data class SplitPdfByLinesEvent(
            val title: String,
            val firstLine: String,
            val lastLine: String,
            val material: MappedMaterialModel
        ): SplitEvent
    }

    sealed interface SplitEffect : UiEffect {

    }

    data class SplitState(
        override var isLoading: Boolean = false,
        var materialList: List<MappedMaterialModel>? = null
    ) : UiState

}