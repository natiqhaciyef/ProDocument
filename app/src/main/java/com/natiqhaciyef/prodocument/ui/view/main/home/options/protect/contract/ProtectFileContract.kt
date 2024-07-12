package com.natiqhaciyef.prodocument.ui.view.main.home.options.protect.contract

import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState

object ProtectFileContract {

    sealed interface ProtectFileEvent : UiEvent {
        data class ProtectFileWithKeyEvent(
            val key: String,
            val material: MappedMaterialModel
        ) : ProtectFileEvent
    }


    sealed interface ProtectFileEffect : UiEffect {

    }

    data class ProtectFileState(
        override var isLoading: Boolean = false,
        var material: MappedMaterialModel? = null
    ) : UiState
}