package com.natiqhaciyef.prodocument.ui.view.main.home.options.compress.viewmodel

import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Quality
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.domain.usecase.material.compress.CompressMaterialUseCase
import com.natiqhaciyef.prodocument.ui.view.main.home.options.compress.contract.CompressContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompressViewModel @Inject constructor(
    private val compressMaterialUseCase: CompressMaterialUseCase
): BaseViewModel<CompressContract.CompressState, CompressContract.CompressEvent, CompressContract.CompressEffect>() {


    override fun onEventUpdate(event: CompressContract.CompressEvent) {
        when(event){
            is CompressContract.CompressEvent.CompressMaterialEvent -> {
                compressMaterial(material = event.material, quality = event.quality)
            }
        }
    }

    private fun compressMaterial(material: MappedMaterialModel, quality: Quality){
        viewModelScope.launch {
            compressMaterialUseCase.operate(material.copy(quality = quality))
        }
    }

    override fun getInitialState(): CompressContract.CompressState =
        CompressContract.CompressState()
}