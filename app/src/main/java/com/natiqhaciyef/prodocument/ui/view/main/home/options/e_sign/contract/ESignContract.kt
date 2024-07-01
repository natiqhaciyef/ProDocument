package com.natiqhaciyef.prodocument.ui.view.main.home.options.e_sign.contract

import android.graphics.Bitmap
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState
import com.natiqhaciyef.uikit.custom.CustomCanvasView

object ESignContract {

    sealed interface ESignEvent : UiEvent {
        data class SignMaterialEvent(
            val material: MappedMaterialModel,
            val eSign: String,
            val bitmap: Bitmap,
            val positionsList: MutableList<Float>,
            val pageNumber: Int
        ) : ESignEvent

        data class ConvertSignToBitmap(
            val view: CustomCanvasView
        ): ESignEvent
    }

    sealed interface ESignEffect : UiEffect {

    }

    data class ESignState(
        override var isLoading: Boolean = false,
        var mappedMaterialModel: MappedMaterialModel? = null,
        var signBitmap: Bitmap? = null
    ) : UiState
}