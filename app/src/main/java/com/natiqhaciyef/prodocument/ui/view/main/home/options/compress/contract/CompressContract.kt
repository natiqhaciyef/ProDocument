package com.natiqhaciyef.prodocument.ui.view.main.home.options.compress.contract

import com.natiqhaciyef.common.model.Quality
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState

object CompressContract {

    sealed class CompressEvent : UiEvent{
        data class CompressMaterialEvent(
            var material: MappedMaterialModel,
            var quality: Quality
        ): CompressEvent()
    }

    sealed class CompressEffect : UiEffect

    data class CompressState(
        override var isLoading: Boolean = false,
        var material: MappedMaterialModel? = null
    ) : UiState
}