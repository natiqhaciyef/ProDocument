package com.natiqhaciyef.prodocument.ui.view.main.files.contract

import android.content.Context
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState
import com.natiqhaciyef.common.model.ParamsUIModel

object FileContract {
    sealed class FileEvent : UiEvent {

        data class GetMaterialById(val id: String) : FileEvent()

        data class GetAllFileParams(val context: Context) : FileEvent()

        data object GetAllMaterials : FileEvent()

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
        var material: MappedMaterialModel? = null,
        var params: List<ParamsUIModel>? = null
    ) : UiState

}