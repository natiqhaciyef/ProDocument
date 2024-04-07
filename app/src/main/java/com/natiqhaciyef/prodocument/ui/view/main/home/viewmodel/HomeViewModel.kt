package com.natiqhaciyef.prodocument.ui.view.main.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.objects.MATERIAL_TOKEN_MOCK_KEY
import com.natiqhaciyef.domain.usecase.material.GetAllMaterialsRemoteUseCase
import com.natiqhaciyef.prodocument.ui.base.BaseUIState
import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllMaterialsRemoteUseCase: GetAllMaterialsRemoteUseCase
) : BaseViewModel() {
    private var _fileState = MutableLiveData<BaseUIState<MappedMaterialModel>>(BaseUIState())
    val fileState: LiveData<BaseUIState<MappedMaterialModel>>
        get() = _fileState

    init {
        getAllOwnFiles()
    }

    fun getAllOwnFiles(token: String = MATERIAL_TOKEN_MOCK_KEY) {
        viewModelScope.launch {
            getAllMaterialsRemoteUseCase.operate(token).collectLatest { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        if (result.data != null) {
                            _fileState.value = _fileState.value?.copy(
                                obj = result.data!!.first(),
                                list = result.data!!,
                                isLoading = false,
                                isSuccess = true,
                                message = null,
                                failReason = null
                            )
                        }
                    }

                    Status.ERROR -> {
                        _fileState.value = _fileState.value?.copy(
                            obj = null,
                            list = listOf(),
                            isLoading = false,
                            isSuccess = false,
                            message = result.message,
                            failReason = result.exception
                        )
                    }
                    Status.LOADING -> {
                        _fileState.value = _fileState.value?.copy(
                            obj = null,
                            list = listOf(),
                            isLoading = true,
                            isSuccess = false,
                            message = null,
                            failReason = null
                        )
                    }
                }
            }
        }
    }
}