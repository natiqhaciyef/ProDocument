package com.natiqhaciyef.prodocument.ui.view.main.premium.contract

import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.mapped.MappedSubscriptionModel
import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState

object PremiumContract {

    sealed interface PremiumEvent : UiEvent {
        data object GetAllSubscriptionPlans : PremiumEvent

        data object Clear: PremiumEvent
    }

    sealed interface PremiumEffect : UiEffect {

    }

    data class PremiumState(
        override var isLoading: Boolean = false,
        var subscriptions: List<MappedSubscriptionModel>? = null,
    ) : UiState
}