package com.natiqhaciyef.prodocument.ui.view.options.merge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.domain.usecase.material.merge.MergeMaterialsUseCase
import com.natiqhaciyef.prodocument.ui.base.BaseUIState
import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import com.natiqhaciyef.prodocument.ui.util.DefaultImplModels
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MergePdfViewModel @Inject constructor(
    private val mergeMaterialsUseCase: MergeMaterialsUseCase
) : BaseViewModel() {
    private val _mergedFileLiveData =
        MutableLiveData<BaseUIState<MappedMaterialModel>>(BaseUIState())
    val mergeFileLiveData: LiveData<BaseUIState<MappedMaterialModel>>
        get() = _mergedFileLiveData

    fun getDefaultMockFile() = DefaultImplModels.mappedMaterialModel

    fun mergeMaterials(list: List<MappedMaterialModel>) {
        viewModelScope.launch {
            mergeMaterialsUseCase.operate(list).collectLatest { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        if (result.data != null)
                            _mergedFileLiveData.value = _mergedFileLiveData.value?.copy(
                                obj = result.data!!,
                                list = listOf(result.data!!),
                                isLoading = false,
                                isSuccess = true,
                                message = null,
                                failReason = null
                            )
                    }

                    Status.ERROR -> {
                        _mergedFileLiveData.value = _mergedFileLiveData.value?.copy(
                            obj = null,
                            list = listOf(),
                            isLoading = false,
                            isSuccess = false,
                            message = result.message,
                            failReason = result.exception
                        )
                    }
                    Status.LOADING -> {
                        _mergedFileLiveData.value = _mergedFileLiveData.value?.copy(
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