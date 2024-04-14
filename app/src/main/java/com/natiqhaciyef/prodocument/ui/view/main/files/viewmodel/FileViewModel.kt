package com.natiqhaciyef.prodocument.ui.view.main.files.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.domain.usecase.material.GetAllMaterialsRemoteUseCase
import com.natiqhaciyef.prodocument.ui.base.BaseUIState
import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import com.natiqhaciyef.prodocument.ui.base.State
import com.natiqhaciyef.prodocument.ui.base.TotalUIState
import com.natiqhaciyef.prodocument.ui.view.main.files.event.FileEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FileViewModel @Inject constructor(
    private val getAllMaterialsRemoteUseCase: GetAllMaterialsRemoteUseCase
) : BaseViewModel<FileEvent>() {

    override fun onEventUpdate(event: FileEvent) {
        when (event) {
            is FileEvent.GetAllMaterials -> getAllMaterials(event.token)
            is FileEvent.GetFileById -> {}
        }
    }

    private fun getAllMaterials(token: String) {
        viewModelScope.launch {
            getAllMaterialsRemoteUseCase.operate(token).collectLatest { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        if (result.data != null) {
                            setBaseState(getCurrentBaseState()
                                .copy(data = TotalUIState.Success(result.data!!)))
                        }
                    }

                    Status.ERROR -> {
                        setBaseState(getCurrentBaseState()
                            .copy(data = TotalUIState
                                .Error(error = result.exception, message = result.message))
                        )
                    }

                    Status.LOADING -> {
                        getCurrentBaseState().copy(data = TotalUIState.Loading)
                    }
                }
            }
        }
    }

    override fun getInitialState(): State = State(TotalUIState.Empty)
}