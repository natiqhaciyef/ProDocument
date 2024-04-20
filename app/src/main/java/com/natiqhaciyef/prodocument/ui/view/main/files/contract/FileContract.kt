package com.natiqhaciyef.prodocument.ui.view.main.files.contract

import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.prodocument.ui.base.UiEffect
import com.natiqhaciyef.prodocument.ui.base.UiEvent
import com.natiqhaciyef.prodocument.ui.base.UiState

object FileContract {
    sealed class FileEvent : UiEvent {

        data class GetFileById(val id: String, val email: String) : FileEvent()

        data class GetAllMaterials(val token: String) : FileEvent()

    }

    sealed class FileEffect : UiEffect {
        data class FindMaterialByIdFailedEffect(
            var message: String? = null,
            var error: Exception? = null
        ): FileEffect()
    }

    data class FileState(
        override var isLoading: Boolean = false,
        var list: List<MappedMaterialModel>? = null,
        var material: MappedMaterialModel? = null
    ) : UiState

}