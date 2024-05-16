package com.natiqhaciyef.prodocument.ui.view.main.home.recent.contract

import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState

object RecentFilesContract {

    sealed class RecentFilesEvent: UiEvent{
        data object GetAllMaterials : RecentFilesEvent()
    }

    sealed class RecentFilesEffect: UiEffect{

    }

    data class RecentFilesState(
        override var isLoading: Boolean = false,
        var list: List<MappedMaterialModel>? = null
    ): UiState
}