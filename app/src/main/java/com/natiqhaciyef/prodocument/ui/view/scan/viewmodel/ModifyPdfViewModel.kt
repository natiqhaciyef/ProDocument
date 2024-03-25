package com.natiqhaciyef.prodocument.ui.view.scan.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.helpers.toJsonString
import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.domain.usecase.MATERIAL_MODEL
import com.natiqhaciyef.domain.usecase.MATERIAL_TOKEN
import com.natiqhaciyef.domain.usecase.material.CreateMaterialByTokenUseCase
import com.natiqhaciyef.prodocument.ui.base.BaseUIState
import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModifyPdfViewModel @Inject constructor(
    // create file for user
    private val createMaterialByTokenUseCase: CreateMaterialByTokenUseCase
) : BaseViewModel() {
    private val _materialState = MutableLiveData<BaseUIState<CRUDModel>>(BaseUIState())
    val materialState: LiveData<BaseUIState<CRUDModel>>
        get() = _materialState

    fun createMaterial(token: MappedTokenModel, material: MappedMaterialModel) {
        if (!token.materialToken.isNullOrEmpty()
            && !token.uid.isNullOrEmpty()
        ) {
            val materialStr = material.toJsonString()
            val requestMap = hashMapOf(
                MATERIAL_TOKEN to token.materialToken!!,
                MATERIAL_MODEL to materialStr
            )

            viewModelScope.launch {
                createMaterialByTokenUseCase.operate(requestMap).collectLatest { result ->
                    when (result.status) {
                        Status.SUCCESS -> {
                            result.data?.let { data ->
                                _materialState.value?.apply {
                                    obj = data
                                    list = listOf(data)
                                    isLoading = false
                                    isSuccess = true
                                    message = result.message
                                    failReason = null
                                }
                            }
                        }

                        Status.ERROR -> {
                            _materialState.value?.apply {
                                obj = null
                                list = listOf()
                                isLoading = false
                                isSuccess = false
                                message = result.message
                                failReason = result.exception
                            }
                        }

                        Status.LOADING -> {
                            _materialState.value?.apply {
                                obj = null
                                list = listOf()
                                isLoading = true
                                isSuccess = false
                                message = null
                                failReason = null
                            }
                        }
                    }
                }
            }
        }
    }


}