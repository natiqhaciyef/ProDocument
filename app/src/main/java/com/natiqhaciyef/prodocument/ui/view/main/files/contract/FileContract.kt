package com.natiqhaciyef.prodocument.ui.view.main.files.contract

import android.content.Context
import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState
import com.natiqhaciyef.common.model.ParamsUIModel
import com.natiqhaciyef.common.model.mapped.MappedFolderModel
import com.natiqhaciyef.domain.network.request.FolderRequest

object FileContract {
    sealed interface FileEvent : UiEvent {

        data class GetMaterialById(val id: String) : FileEvent

        data class GetAllFileParams(val context: Context) : FileEvent

        data object GetAllMaterials : FileEvent

        data object GetAllFolders : FileEvent

        data class GetMaterialsByFolderId(val folderId: String) : FileEvent

        data class CreateFolder(val folderRequest: FolderRequest) : FileEvent

        data class RemoveMaterial(val materialId: String) : FileEvent

        data class SortMaterials(
            var list: MutableList<MappedMaterialModel>,
            var type: String
        ) : FileEvent

        data class FileFilterEvent(
            var list: MutableList<MappedMaterialModel>,
            var text: String
        ): FileEvent

        data object Clear : FileEvent
    }

    sealed interface FileEffect : UiEffect {
        data class FindMaterialByIdFailedEffect(
            var message: String? = null,
            var error: Exception? = null
        ) : FileEffect

        data object FilteredFileNotFoundEffect : FileEffect
    }

    data class FileState(
        override var isLoading: Boolean = false,
        var list: List<MappedMaterialModel>? = null,
        var folders: List<MappedFolderModel>? = null,
        var material: MappedMaterialModel? = null,
        var result: CRUDModel? = null,
        var params: List<ParamsUIModel>? = null
    ) : UiState

}