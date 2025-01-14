package com.natiqhaciyef.prodocument.ui.view.main.modify.contract

import android.content.Context
import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.BaseActivity
import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState
import com.natiqhaciyef.core.model.CategoryItem

object ModifyPdfContract {

    sealed interface ModifyPdfEvent : UiEvent {
        data class CreateMaterialEvent(var material: MappedMaterialModel) : ModifyPdfEvent

        data class GetShareOptions(val context: Context,
                                   var activity: BaseActivity<*>
        ): ModifyPdfEvent

        data class WatermarkMaterialEvent(var title: String,
                                          var mappedMaterialModel: MappedMaterialModel,
                                          var watermark: String): ModifyPdfEvent
    }

    sealed interface ModifyPdfEffect : UiEffect {
        data class CreateMaterialFailEffect(
            var message: String? = null,
            var exception: Exception? = null
        ): ModifyPdfEffect

    }

    data class ModifyPdfState(
        override var isLoading: Boolean = false,
        var result: CRUDModel? = null,
        var material: MappedMaterialModel? = null,
        var optionsList: List<CategoryItem>? = null
    ) : UiState
}