package com.natiqhaciyef.prodocument.ui.view.main.home.options.e_sign.contract

import android.graphics.Bitmap
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState

object ESignContract {

    sealed interface ESignEvent : UiEvent {
        data class SignMaterialEvent(
            val material: MappedMaterialModel,
            val eSign: String,
            val bitmap: Bitmap
        ) : ESignEvent
    }

    sealed interface ESignEffect : UiEffect {

    }

    data class ESignState(
        override var isLoading: Boolean = false,
        var mappedMaterialModel: MappedMaterialModel? = null
    ) : UiState
}