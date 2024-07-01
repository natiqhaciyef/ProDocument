package com.natiqhaciyef.prodocument.ui.view.main.home.options.merge.viewmodel

import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.domain.usecase.MATERIAL_LIST
import com.natiqhaciyef.domain.usecase.MATERIAL_TITLE
import com.natiqhaciyef.domain.usecase.material.MergeMaterialsUseCase
import com.natiqhaciyef.prodocument.ui.view.main.home.options.merge.contract.MergePdfContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MergePdfViewModel @Inject constructor(
    private val mergeMaterialsUseCase: MergeMaterialsUseCase
) : BaseViewModel<MergePdfContract.MergePdfState, MergePdfContract.MergePdfEvent, MergePdfContract.MergePdfEffect>() {

    override fun onEventUpdate(event: MergePdfContract.MergePdfEvent) {
        when(event){
            is MergePdfContract.MergePdfEvent.MergeMaterialsEvent -> mergeMaterials(event.title, event.list)
        }
    }

    private fun mergeMaterials(title: String, list: List<MappedMaterialModel>) {
        val map = mapOf(MATERIAL_TITLE to title, MATERIAL_LIST to list)
        viewModelScope.launch {
            mergeMaterialsUseCase.operate(map).collectLatest { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        if (result.data != null)
                            setBaseState(getCurrentBaseState().copy(isLoading = false, mappedMaterialModel = result.data))
                    }

                    Status.ERROR -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = false))
                        postEffect(
                            MergePdfContract.MergePdfEffect.MergeFailedEffect(
                            message = result.message,
                            exception = result.exception
                        ))
                    }
                    Status.LOADING -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = true))
                    }
                }
            }
        }
    }

    override fun getInitialState(): MergePdfContract.MergePdfState = MergePdfContract.MergePdfState()
}