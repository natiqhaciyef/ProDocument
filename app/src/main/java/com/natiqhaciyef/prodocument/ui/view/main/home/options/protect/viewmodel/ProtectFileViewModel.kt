package com.natiqhaciyef.prodocument.ui.view.main.home.options.protect.viewmodel

import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.domain.usecase.material.protect.ProtectMaterialUseCase
import com.natiqhaciyef.prodocument.ui.view.main.home.options.protect.contract.ProtectFileContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProtectFileViewModel @Inject constructor(
    private val protectMaterialUseCase: ProtectMaterialUseCase
) : BaseViewModel<ProtectFileContract.ProtectFileState, ProtectFileContract.ProtectFileEvent, ProtectFileContract.ProtectFileEffect>() {

    override fun onEventUpdate(event: ProtectFileContract.ProtectFileEvent) {
        when (event) {
            is ProtectFileContract.ProtectFileEvent.ProtectFileWithKeyEvent -> {
                protectMaterial(material = event.material, key = event.key)
            }
            else -> {}
        }
    }

    private fun protectMaterial(material: MappedMaterialModel, key: String) {
        viewModelScope.launch {
            protectMaterialUseCase.operate(material.copy(isProtected = true, protectionKey = key))
                .collectLatest { result ->
                    when (result.status) {
                        Status.SUCCESS -> {
                            if (result.data != null)
                                setBaseState(
                                    getCurrentBaseState().copy(
                                        isLoading = false,
                                        material = result.data
                                    )
                                )
                        }

                        Status.ERROR -> {
                            setBaseState(getCurrentBaseState().copy(isLoading = false))
                            // effect
                        }

                        Status.LOADING -> {
                            setBaseState(getCurrentBaseState().copy(isLoading = true))
                        }
                    }
                }
        }
    }

    override fun getInitialState(): ProtectFileContract.ProtectFileState =
        ProtectFileContract.ProtectFileState()
}