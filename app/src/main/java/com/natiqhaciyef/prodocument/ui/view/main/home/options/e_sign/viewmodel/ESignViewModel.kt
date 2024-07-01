package com.natiqhaciyef.prodocument.ui.view.main.home.options.e_sign.viewmodel

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.view.drawToBitmap
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.domain.usecase.CURRENT_PAGE_NUMBER
import com.natiqhaciyef.domain.usecase.MATERIAL_E_SIGN
import com.natiqhaciyef.domain.usecase.MATERIAL_E_SIGN_BITMAP
import com.natiqhaciyef.domain.usecase.MATERIAL_MODEL
import com.natiqhaciyef.domain.usecase.POSITIONS_LIST
import com.natiqhaciyef.domain.usecase.material.ESignMaterialUseCase
import com.natiqhaciyef.uikit.custom.CustomCanvasView
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
        when (event) {
            is ESignContract.ESignEvent.SignMaterialEvent -> {
                with(event){
                    signMaterial(
                        material = material,
                        sign = eSign,
                        signBitmap = bitmap,
                        positionsList = positionsList,
                        pageNumber = pageNumber
                    )
                }
            }

            is ESignContract.ESignEvent.ConvertSignToBitmap -> {
                getDrawingBitmap(view = event.view)
            }

            else -> {}
        }
    }


    private fun signMaterial(
        material: MappedMaterialModel,
        sign: String,
        signBitmap: Bitmap,
        positionsList: MutableList<Float>,
        pageNumber: Int
    ) {
        val map = mutableMapOf<String, Any>()
        map[MATERIAL_MODEL] = material
        map[MATERIAL_E_SIGN] = sign
        map[MATERIAL_E_SIGN_BITMAP] = signBitmap
        map[POSITIONS_LIST] = positionsList
        map[CURRENT_PAGE_NUMBER] = pageNumber

        viewModelScope.launch {
            eSignMaterialUseCase.operate(map).collectLatest { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        if (result.data != null)
                            setBaseState(
                                getCurrentBaseState().copy(
                                    isLoading = false,
                                    mappedMaterialModel = result.data
                                )
                            )
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

    private fun getDrawingBitmap(view: CustomCanvasView) {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas) // Draw the DrawView's content onto the Bit map
        val bmp = view.drawToBitmap()
        setBaseState(getCurrentBaseState().copy(signBitmap = bmp))
    }


    override fun getInitialState(): ESignContract.ESignState =
        ESignContract.ESignState()
}