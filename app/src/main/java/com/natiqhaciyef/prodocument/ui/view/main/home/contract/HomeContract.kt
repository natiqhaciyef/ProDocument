package com.natiqhaciyef.prodocument.ui.view.main.home.contract

import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState

object HomeContract {

    sealed class HomeEvent: UiEvent {
        data object GetAllMaterials : HomeEvent()

        data class GetMaterialById(val id: String) : HomeEvent()

        data class RemoveMaterial(val materialId: String): HomeEvent()
    }

    sealed class HomeEffect: UiEffect {
        data class MaterialListLoadingFailedEffect(
            var message: String? = null,
            var error: Exception? = null
        ) : HomeEffect()

        data class FindMaterialByIdFailedEffect(
            var message: String? = null,
            var error: Exception? = null
        ): HomeEffect()
    }

    data class HomeUiState(
        var list: List<MappedMaterialModel>? = null,
        var material: MappedMaterialModel? = null,
        var result: CRUDModel? = null,
        override var isLoading: Boolean = false,
    ): UiState
}