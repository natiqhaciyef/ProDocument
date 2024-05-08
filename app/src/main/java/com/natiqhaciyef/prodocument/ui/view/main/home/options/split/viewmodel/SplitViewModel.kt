package com.natiqhaciyef.prodocument.ui.view.main.home.options.split.viewmodel

import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.domain.usecase.MATERIAL_FIRST_LINE
import com.natiqhaciyef.domain.usecase.MATERIAL_LAST_LINE
import com.natiqhaciyef.domain.usecase.MATERIAL_MODEL
import com.natiqhaciyef.domain.usecase.MATERIAL_TITLE
import com.natiqhaciyef.domain.usecase.material.split.SplitMaterialUseCase
import com.natiqhaciyef.prodocument.ui.view.main.home.options.split.contract.SplitContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplitViewModel @Inject constructor(
    private val splitMaterialUseCase: SplitMaterialUseCase
) : BaseViewModel<SplitContract.SplitState, SplitContract.SplitEvent, SplitContract.SplitEffect>() {

    override fun onEventUpdate(event: SplitContract.SplitEvent) {
        when (event) {
            is SplitContract.SplitEvent.SplitPdfByLinesEvent -> {
                splitMaterial(
                    title = event.title,
                    material = event.material,
                    firstLine = event.firstLine,
                    lastLine = event.firstLine
                )
            }
        }
    }

    private fun splitMaterial(
        title: String,
        material: MappedMaterialModel,
        firstLine: String,
        lastLine: String
    ) {
        val map = mutableMapOf<String, Any>()
        map[MATERIAL_MODEL] = material
        map[MATERIAL_FIRST_LINE] = firstLine
        map[MATERIAL_LAST_LINE] = lastLine
        map[MATERIAL_TITLE] = title

        viewModelScope.launch {
            splitMaterialUseCase.operate(map).collectLatest { result ->
                when (result.status) {
                    Status.LOADING -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = true, materialList = null))
                    }

                    Status.SUCCESS -> {
                        if (result.data != null)
                            setBaseState(getCurrentBaseState().copy(isLoading = false, materialList = result.data!!))
                    }

                    Status.ERROR -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = false, materialList = null))
                    }
                }
            }
        }
    }


    override fun getInitialState(): SplitContract.SplitState = SplitContract.SplitState()
}