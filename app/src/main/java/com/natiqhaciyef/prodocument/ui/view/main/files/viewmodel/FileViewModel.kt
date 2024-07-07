package com.natiqhaciyef.prodocument.ui.view.main.files.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.domain.usecase.material.GetAllMaterialsRemoteUseCase
import com.natiqhaciyef.domain.usecase.material.GetMaterialByIdRemoteUseCase
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.prodocument.ui.view.main.files.FilesFragment
import com.natiqhaciyef.prodocument.ui.view.main.files.contract.FileContract
import com.natiqhaciyef.common.model.FieldType
import com.natiqhaciyef.common.model.ParamsUIModel
import com.natiqhaciyef.prodocument.ui.util.getOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FileViewModel @Inject constructor(
    private val getAllMaterialsRemoteUseCase: GetAllMaterialsRemoteUseCase,
    private val getMaterialByIdRemoteUseCase: GetMaterialByIdRemoteUseCase
) : BaseViewModel<FileContract.FileState, FileContract.FileEvent, FileContract.FileEffect>() {

    override fun onEventUpdate(event: FileContract.FileEvent) {
        when (event) {
            is FileContract.FileEvent.GetAllMaterials -> getAllMaterials() /* event.email */
            is FileContract.FileEvent.GetMaterialById -> {
                getMaterialById(event.id)
            }

            is FileContract.FileEvent.SortMaterials -> {
                sortMaterials(event.list, event.type)
            }

            is FileContract.FileEvent.FileFilterEvent -> {
                filterList(event.list, event.text)
            }

            is FileContract.FileEvent.GetAllFileParams -> {
                generateParams(event.context)
            }
        }
    }

    private fun getAllMaterials() {
        viewModelScope.launch {
            getAllMaterialsRemoteUseCase.invoke().collectLatest { result ->
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
        }else{
            postEffect(FileContract.FileEffect.FilteredFileNotFoundEffect)
        }
    }

    private fun generateParams(ctx: Context){
        setBaseState(getCurrentBaseState().copy(params = getOptions(ctx)))
    }

    override fun getInitialState(): FileContract.FileState = FileContract.FileState()
}