package com.natiqhaciyef.prodocument.ui.view.main.home.options.merge.contract

import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState
import java.lang.Exception

object MergePdfContract {
    sealed interface MergePdfEvent : UiEvent {
        data class MergeMaterialsEvent(var title: String, var list: List<MappedMaterialModel>) : MergePdfEvent
    }

    sealed interface MergePdfEffect : UiEffect {
        data class MergeFailedEffect(
            var message: String? = null,
            var exception: Exception? = null
        ) : MergePdfEffect
    }

    data class MergePdfState(
        override var isLoading: Boolean = false,
        var mappedMaterialModel: MappedMaterialModel? = null,
    ) : UiState
}