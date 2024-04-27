package com.natiqhaciyef.prodocument.ui.view.main.home.options.merge.contract

import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.prodocument.ui.base.UiEffect
import com.natiqhaciyef.prodocument.ui.base.UiEvent
import com.natiqhaciyef.prodocument.ui.base.UiState
import java.lang.Exception

object MergePdfContract {
    sealed class MergePdfEvent : UiEvent {
        data class MergeMaterialsEvent(var title: String, var list: List<MappedMaterialModel>) : MergePdfEvent()
    }

    sealed class MergePdfEffect : UiEffect {
        data class MergeFailedEffect(
            var message: String? = null,
            var exception: Exception? = null
        ) : MergePdfEffect()
    }

    data class MergePdfState(
        override var isLoading: Boolean = false,
        var mappedMaterialModel: MappedMaterialModel? = null,
    ) : UiState
}