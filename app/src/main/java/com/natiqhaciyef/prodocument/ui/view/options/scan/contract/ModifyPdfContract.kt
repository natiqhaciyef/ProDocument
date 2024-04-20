package com.natiqhaciyef.prodocument.ui.view.options.scan.contract

import android.content.Context
import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.prodocument.ui.base.UiEffect
import com.natiqhaciyef.prodocument.ui.base.UiEvent
import com.natiqhaciyef.prodocument.ui.base.UiState
import com.natiqhaciyef.prodocument.ui.model.CategoryItem

object ModifyPdfContract {

    sealed class ModifyPdfEvent : UiEvent {
        data class CreateMaterialEvent(
            var token: MappedTokenModel,
            var material: MappedMaterialModel
        ) : ModifyPdfEvent()

        data class GetShareOptions(val context: Context): ModifyPdfEvent()
    }

    sealed class ModifyPdfEffect : UiEffect {
        data class CreateMaterialFailEffect(
            var message: String? = null,
            var exception: Exception? = null
        ): ModifyPdfEffect()

    }

    data class ModifyPdfState(
        override var isLoading: Boolean = false,
        var result: CRUDModel? = null,
        var optionsList: List<CategoryItem>? = null
    ) : UiState
}