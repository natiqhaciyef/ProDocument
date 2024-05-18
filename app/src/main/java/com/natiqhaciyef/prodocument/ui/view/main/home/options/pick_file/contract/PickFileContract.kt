package com.natiqhaciyef.prodocument.ui.view.main.home.options.pick_file.contract

import com.natiqhaciyef.common.model.Quality
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState

object PickFileContract {

    sealed interface PickFileEvent : UiEvent {
        data class ProtectFileEvent(
            var material: MappedMaterialModel,
            var key: String
        ) : PickFileEvent

        data class CompressMaterialEvent(
            var material: MappedMaterialModel,
            var quality: Quality
        ) : PickFileEvent
    }

    sealed interface PickFileEffect : UiEffect {

    }

    data class PickFileState(
        override var isLoading: Boolean = false,
        var material: MappedMaterialModel? = null
    ) : UiState
}