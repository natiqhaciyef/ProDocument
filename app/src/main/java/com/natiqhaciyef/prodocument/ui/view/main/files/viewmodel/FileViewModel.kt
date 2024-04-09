package com.natiqhaciyef.prodocument.ui.view.main.files.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.domain.usecase.material.GetAllMaterialsRemoteUseCase
import com.natiqhaciyef.prodocument.ui.base.BaseUIState
import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FileViewModel @Inject constructor(
    private val getAllMaterialsRemoteUseCase: GetAllMaterialsRemoteUseCase
) : BaseViewModel() {
    private val _materialsLiveData =
        MutableLiveData<BaseUIState<MappedMaterialModel>>(BaseUIState())
    val materialsLiveData: LiveData<BaseUIState<MappedMaterialModel>>
        get() = _materialsLiveData

    private fun getAllMaterials(token: String) {
        viewModelScope.launch {
            getAllMaterialsRemoteUseCase.operate(token).collectLatest { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        if (result.data != null) {
                            _materialsLiveData.value = _materialsLiveData.value?.copy(
                                obj = result.data!!.first(),
                                list = result.data!!,
                                isLoading = false,
                                isSuccess = true,
                                message = null,
                                failReason = null
                            )
//                            setBaseState()
                        }
                    }

                    Status.ERROR -> {
                        _materialsLiveData.value = _materialsLiveData.value?.copy(
                            obj = null,
                            list = listOf(),
                            isLoading = false,
                            isSuccess = false,
                            message = result.message,
                            failReason = result.exception
                        )
                    }

                    Status.LOADING -> {
                        _materialsLiveData.value = _materialsLiveData.value?.copy(
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

//    override fun getInitialState(): BaseUIState<MappedMaterialModel> = BaseUIState()
}