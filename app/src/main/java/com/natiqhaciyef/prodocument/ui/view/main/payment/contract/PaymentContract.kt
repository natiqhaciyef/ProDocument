package com.natiqhaciyef.prodocument.ui.view.main.payment.contract

import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.payment.MappedPaymentChequeModel
import com.natiqhaciyef.common.model.payment.MappedPaymentModel
import com.natiqhaciyef.common.model.payment.MappedPaymentPickModel
import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState

object PaymentContract {

    sealed interface PaymentEvent : UiEvent {
        data class PickPaymentMethod(
            val pickedPaymentMethod: MappedPaymentPickModel
        ): PaymentEvent

        data class AddNewPaymentMethod(
            val paymentModel: MappedPaymentModel
        ) : PaymentEvent

        data class GetPaymentData(
            val paymentModel: MappedPaymentModel,
            val pickedPlan: String,
            val isSaved: Boolean = false
        ): PaymentEvent

        data class PayForPlan(val cheque: MappedPaymentChequeModel): PaymentEvent

        data class GetChequePdf(val chequeId: String): PaymentEvent

        data object GetAllStoredPaymentMethods: PaymentEvent
    }

    sealed interface PaymentEffect : UiEffect {

    }

    data class PaymentState(
        override var isLoading: Boolean = false,
        var paymentMethodsList: List<MappedPaymentModel>? = null,
        var pickedPayment: MappedPaymentPickModel? = null,
        var mappedPaymentModel: MappedPaymentModel? = null,
        var cheque: MappedPaymentChequeModel? = null,
        var chequePayload: String? = null,
        var paymentResult: CRUDModel? = null,
    ) : UiState
}