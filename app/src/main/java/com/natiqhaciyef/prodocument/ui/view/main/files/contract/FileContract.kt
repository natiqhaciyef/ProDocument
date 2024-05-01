package com.natiqhaciyef.prodocument.ui.view.main.files.contract

import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.prodocument.ui.base.UiEffect
import com.natiqhaciyef.prodocument.ui.base.UiEvent
import com.natiqhaciyef.prodocument.ui.base.UiState

object FileContract {
    sealed class FileEvent : UiEvent {

        data class GetMaterialById(val id: String, val email: String) : FileEvent()

        data class GetAllMaterials(val email: String) : FileEvent()

        data class SortMaterials(
            var list: MutableList<MappedMaterialModel>,
            var type: String
        ) : FileEvent()

        data class FileFilterEvent(
            var list: MutableList<MappedMaterialModel>,
            var text: String
        ): FileEvent()
    }

    sealed class FileEffect : UiEffect {
        data class FindMaterialByIdFailedEffect(
            var message: String? = null,
            var error: Exception? = null
        ) : FileEffect()

        data object FilteredFileNotFoundEffect : FileEffect()
    }

    data class FileState(
        override var isLoading: Boolean = false,
        var list: List<MappedMaterialModel>? = null,
        var material: MappedMaterialModel? = null
    ) : UiState

}