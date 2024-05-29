package com.natiqhaciyef.prodocument.ui.view.payment.contract

import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.mapped.MappedPaymentModel
import com.natiqhaciyef.common.model.payment.PaymentPickModel
import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState

object PaymentContract {

    sealed interface PaymentEvent : UiEvent {
        data class PickPaymentMethod(
            val pickedPaymentMethod: PaymentPickModel
        ): PaymentEvent

        data class AddNewPaymentMethod(
            val paymentModel: MappedPaymentModel
        ) : PaymentEvent

        data class PayForPlan(
            val paymentModel: MappedPaymentModel,
            val isSaved: Boolean = false
        ): PaymentEvent

        data object GetAllStoredPaymentMethods: PaymentEvent
    }

    sealed interface PaymentEffect : UiEffect {

    }

    data class PaymentState(
        override var isLoading: Boolean = false,
        var pickedPayment: PaymentPickModel? = null,
        var mappedPaymentModel: MappedPaymentModel? = null,
//        var paymentResult: CRUDModel? = null, create CheckModel
    ) : UiState
}