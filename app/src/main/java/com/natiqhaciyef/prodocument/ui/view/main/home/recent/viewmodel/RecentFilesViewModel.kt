package com.natiqhaciyef.prodocument.ui.view.main.home.recent.viewmodel

import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.domain.usecase.material.GetAllMaterialsRemoteUseCase
import com.natiqhaciyef.prodocument.ui.view.main.home.recent.contract.RecentFilesContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentFilesViewModel @Inject constructor(
    private var getAllMaterialsRemoteUseCase: GetAllMaterialsRemoteUseCase
) : BaseViewModel<RecentFilesContract.RecentFilesState, RecentFilesContract.RecentFilesEvent, RecentFilesContract.RecentFilesEffect>() {

    override fun onEventUpdate(event: RecentFilesContract.RecentFilesEvent) {
        when (event) {
            is RecentFilesContract.RecentFilesEvent.GetAllMaterials -> getAllMaterials()
        }
    }

    private fun getAllMaterials() {
        viewModelScope.launch {
            getAllMaterialsRemoteUseCase.invoke().collectLatest { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        if (result.data != null){
                            setBaseState(getCurrentBaseState().copy(isLoading = false, list = result.data))
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

    override fun getInitialState(): RecentFilesContract.RecentFilesState =
        RecentFilesContract.RecentFilesState()
}