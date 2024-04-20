package com.natiqhaciyef.prodocument.ui.view.main.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.objects.MATERIAL_TOKEN_MOCK_KEY
import com.natiqhaciyef.domain.usecase.MATERIAL_ID
import com.natiqhaciyef.domain.usecase.MATERIAL_TOKEN
import com.natiqhaciyef.domain.usecase.material.GetAllMaterialsRemoteUseCase
import com.natiqhaciyef.domain.usecase.material.GetMaterialByIdRemoteUseCase
import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
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
                getAllOwnFiles(event.token)
            }

            is HomeContract.HomeEvent.GetMaterialById -> {
                getMaterialById(token = event.token, id = event.id)
            }
        }
    }

    private fun getAllOwnFiles(token: String = MATERIAL_TOKEN_MOCK_KEY) {
        viewModelScope.launch {
            getAllMaterialsRemoteUseCase.operate(token).collectLatest { result ->
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

    private fun getMaterialById(token: String, id: String) {
        val request = mapOf(MATERIAL_ID to id, MATERIAL_TOKEN to token)
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