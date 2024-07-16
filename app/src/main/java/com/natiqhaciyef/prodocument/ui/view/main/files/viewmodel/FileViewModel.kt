package com.natiqhaciyef.prodocument.ui.view.main.files.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.domain.network.request.FolderRequest
import com.natiqhaciyef.domain.usecase.material.CreateFolderUseCase
import com.natiqhaciyef.domain.usecase.material.GetAllFoldersUseCase
import com.natiqhaciyef.domain.usecase.material.GetAllMaterialsWithoutFolderUseCase
import com.natiqhaciyef.domain.usecase.material.GetMaterialByIdRemoteUseCase
import com.natiqhaciyef.domain.usecase.material.GetMaterialsByFolderIdUseCase
import com.natiqhaciyef.prodocument.ui.view.main.files.FilesFragment
import com.natiqhaciyef.prodocument.ui.view.main.files.contract.FileContract
import com.natiqhaciyef.domain.usecase.material.RemoveMaterialByIdUseCase
import com.natiqhaciyef.prodocument.ui.util.getOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FileViewModel @Inject constructor(
    private val getAllFoldersUseCase: GetAllFoldersUseCase,
    private val getMaterialByIdRemoteUseCase: GetMaterialByIdRemoteUseCase,
    private val getMaterialsByFolderId: GetMaterialsByFolderIdUseCase,
    private val getAllMaterialsWithoutFolderUseCase: GetAllMaterialsWithoutFolderUseCase,
    private val removeMaterialByIdUseCase: RemoveMaterialByIdUseCase,
    private val createFolderUseCase: CreateFolderUseCase
) : BaseViewModel<FileContract.FileState, FileContract.FileEvent, FileContract.FileEffect>() {

    override fun onEventUpdate(event: FileContract.FileEvent) {
        when (event) {
            is FileContract.FileEvent.GetAllMaterials -> getAllMaterials() /* event.email */

            is FileContract.FileEvent.GetAllFolders -> getAllFolders()

            is FileContract.FileEvent.GetMaterialById -> getMaterialById(event.id)

            is FileContract.FileEvent.GetMaterialsByFolderId -> getAllMaterialByFolderId(event.folderId)

            is FileContract.FileEvent.SortMaterials -> sortMaterials(event.list, event.type)

            is FileContract.FileEvent.FileFilterEvent -> filterList(event.list, event.text)

            is FileContract.FileEvent.GetAllFileParams -> generateParams(event.context)

            is FileContract.FileEvent.RemoveMaterial -> removeMaterial(event.materialId)

            is FileContract.FileEvent.CreateFolder -> createFolder(event.folderRequest)

            is FileContract.FileEvent.Clear -> clearState()
        }
    }

    private fun getAllMaterials() {
        viewModelScope.launch {
            getAllMaterialsWithoutFolderUseCase.invoke().collectLatest { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        if (result.data != null) {
                            setBaseState(getCurrentBaseState().copy(list = result.data!!, isLoading = false))
                        }
                    }

                    Status.ERROR -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = false))
                    }

                    Status.LOADING -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = true))
                    }
                }
            }
        }
    }

    private fun getAllFolders() {
        viewModelScope.launch {
            getAllFoldersUseCase.invoke().collectLatest { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        if (result.data != null) {
                            setBaseState(getHoldState().copy(folders = result.data!!, isLoading = false))
                        }
                    }

                    Status.ERROR -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = false))
                    }

                    Status.LOADING -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = true))
                    }
                }
            }
        }
    }

    private fun getMaterialById(id: String) {
        viewModelScope.launch {
            getMaterialByIdRemoteUseCase.operate(id).collectLatest { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        if (result.data != null)
                            setBaseState(
                                getCurrentBaseState().copy(
                                    isLoading = false,
                                    material = result.data!!
                                )
                            )
                    }

                    Status.ERROR -> {
                        postEffect(
                            FileContract.FileEffect
                                .FindMaterialByIdFailedEffect(result.message, result.exception)
                        )
                        setBaseState(getCurrentBaseState().copy(isLoading = false))
                    }

                    Status.LOADING -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = true))
                    }
                }
            }
        }
    }

    private fun getAllMaterialByFolderId(folderId: String) {
        getMaterialsByFolderId.operate(folderId).onEach { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    if (result.data != null)
                        setBaseState(
                            getCurrentBaseState().copy(
                                isLoading = false,
                                list = result.data!!
                            )
                        )
                }

                Status.ERROR -> {
                    postEffect(
                        FileContract.FileEffect
                            .FindMaterialByIdFailedEffect(result.message, result.exception)
                    )
                    setBaseState(getCurrentBaseState().copy(isLoading = false))
                }

                Status.LOADING -> {
                    setBaseState(getCurrentBaseState().copy(isLoading = true))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun sortMaterials(
        list: MutableList<MappedMaterialModel>,
        type: String
    ) {
        when (type) {
            FilesFragment.A2Z -> list.sortBy { it.title }
            FilesFragment.Z2A -> list.sortByDescending { it.title }
            else -> mutableListOf<MappedMaterialModel>()
        }
        setBaseState(
            getCurrentBaseState().copy(
                isLoading = false,
                list = list
            )
        )
    }

    private fun filterList(list: MutableList<MappedMaterialModel>, text: String) {
        if (list.any { it.title.startsWith(text) }) {
            setBaseState(
                getCurrentBaseState().copy(
                    isLoading = false,
                    list = list.filter { it.title.startsWith(text) })
            )
        } else {
            postEffect(FileContract.FileEffect.FilteredFileNotFoundEffect)
        }
    }

    private fun removeMaterial(materialId: String) {
        removeMaterialByIdUseCase.operate(materialId).onEach { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    if (result.data != null)
                        setBaseState(getCurrentBaseState().copy(result = result.data, isLoading = false))
                }

                Status.ERROR -> {
                    setBaseState(getCurrentBaseState().copy(isLoading = false))
                }

                Status.LOADING -> {
                    setBaseState(getCurrentBaseState().copy(isLoading = true))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun createFolder(folderRequest: FolderRequest) {
        createFolderUseCase.operate(folderRequest).onEach { result ->
            when (result.status) {
                Status.LOADING -> {
                    setBaseState(getCurrentBaseState().copy(isLoading = true))
                }

                Status.SUCCESS -> {
                    if (result.data != null)
                        setBaseState(getCurrentBaseState().copy(isLoading = false, result = result.data))
                }

                Status.ERROR -> {
                    setBaseState(getCurrentBaseState().copy(isLoading = false))
                }
            }

        }.launchIn(viewModelScope)
    }

    private fun generateParams(ctx: Context) {
        setBaseState(getCurrentBaseState().copy(params = getOptions(ctx)))
    }

    private fun clearState() {
        setBaseState(getCurrentBaseState())
    }

    override fun getInitialState(): FileContract.FileState = FileContract.FileState()
}