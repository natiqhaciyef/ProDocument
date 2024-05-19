package com.natiqhaciyef.prodocument.ui.view.main.home.options.e_sign.viewmodel

import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.domain.usecase.MATERIAL_E_SIGN
import com.natiqhaciyef.domain.usecase.MATERIAL_MODEL
import com.natiqhaciyef.domain.usecase.material.e_sign.ESignMaterialUseCase
import com.natiqhaciyef.prodocument.ui.view.main.home.options.e_sign.contract.ESignContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ESignViewModel @Inject constructor(
    private val eSignMaterialUseCase: ESignMaterialUseCase
) : BaseViewModel<ESignContract.ESignState, ESignContract.ESignEvent, ESignContract.ESignEffect>() {

    override fun onEventUpdate(event: ESignContract.ESignEvent) {
        when(event){
            is ESignContract.ESignEvent.SignMaterialEvent -> {
                signMaterial(material = event.material, sign = event.eSign)
            }
            else -> {}
        }
    }


    private fun signMaterial(material: MappedMaterialModel, sign: String){
        val map = mutableMapOf<String, Any>()
        map[MATERIAL_MODEL] = material
        map[MATERIAL_E_SIGN] = sign

        viewModelScope.launch {
            eSignMaterialUseCase.operate(map).collectLatest { result ->
                when(result.status){
                    Status.SUCCESS -> {
                        if (result.data != null)
                            setBaseState(getCurrentBaseState().copy(isLoading = false, mappedMaterialModel = result.data))
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


    override fun getInitialState(): ESignContract.ESignState =
        ESignContract.ESignState()
}