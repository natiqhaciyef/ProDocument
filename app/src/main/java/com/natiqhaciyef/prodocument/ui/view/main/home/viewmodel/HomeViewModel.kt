package com.natiqhaciyef.prodocument.ui.view.main.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.domain.usecase.material.GetAllMaterialsRemoteUseCase
import com.natiqhaciyef.domain.usecase.material.GetMaterialByIdRemoteUseCase
import com.natiqhaciyef.prodocument.ui.view.main.home.contract.HomeContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllMaterialsRemoteUseCase: GetAllMaterialsRemoteUseCase,
    private val getMaterialByIdRemoteUseCase: GetMaterialByIdRemoteUseCase
) : BaseViewModel<HomeContract.HomeUiState, HomeContract.HomeEvent, HomeContract.HomeEffect>() {

    override fun onEventUpdate(event: HomeContract.HomeEvent) {
        when (event) {
            is HomeContract.HomeEvent.GetAllMaterials -> {
                getAllOwnFiles()
            }

            is HomeContract.HomeEvent.GetMaterialById -> {
                getMaterialById(id = event.id)
            }
        }
    }

    private fun getAllOwnFiles() {
        viewModelScope.launch {
            getAllMaterialsRemoteUseCase.invoke().collectLatest { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        if (result.data != null) {
                            setBaseState(
                                getCurrentBaseState().copy(
                                    list = result.data!!,
                                    isLoading = false
                                )
                            )
                        }
                    }

                    Status.ERROR -> {
                        postEffect(
                            HomeContract.HomeEffect.MaterialListLoadingFailedEffect(
                                result.message,
                                result.exception
                            )
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
                            HomeContract.HomeEffect.FindMaterialByIdFailedEffect(
                                result.message,
                                result.exception
                            )
                        )
                    }

                    Status.LOADING -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = true))
                    }
                }
            }
        }
    }

    override fun getInitialState(): HomeContract.HomeUiState = HomeContract.HomeUiState()
}