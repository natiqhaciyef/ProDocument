package com.natiqhaciyef.prodocument.ui.view.main.files.viewmodel

import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.domain.usecase.MATERIAL_ID
import com.natiqhaciyef.domain.usecase.MATERIAL_TOKEN
import com.natiqhaciyef.domain.usecase.USER_EMAIL
import com.natiqhaciyef.domain.usecase.material.GetAllMaterialsRemoteUseCase
import com.natiqhaciyef.domain.usecase.material.GetMaterialByIdRemoteUseCase
import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import com.natiqhaciyef.prodocument.ui.view.main.files.contract.FileContract
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
            is FileContract.FileEvent.GetAllMaterials -> getAllMaterials(event.token)
            is FileContract.FileEvent.GetFileById -> { getMaterialById(event.email, event.id) }
        }
    }

    private fun getAllMaterials(email: String) {
        viewModelScope.launch {
            getAllMaterialsRemoteUseCase.operate(email).collectLatest { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        if (result.data != null) {
                            setBaseState(getCurrentBaseState().copy(list = result.data!!))
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

    private fun getMaterialById(token: String, id: String) {
        val request = mapOf(MATERIAL_ID to id, USER_EMAIL to token)
        viewModelScope.launch {
            getMaterialByIdRemoteUseCase.operate(request).collectLatest { result ->
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
                    }

                    Status.LOADING -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = true))
                    }
                }
            }
        }
    }

    override fun getInitialState(): FileContract.FileState = FileContract.FileState()
}