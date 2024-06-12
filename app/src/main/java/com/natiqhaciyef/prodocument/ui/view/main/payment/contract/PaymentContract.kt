package com.natiqhaciyef.prodocument.ui.view.main.payment.contract

import android.content.Context
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.mapped.MappedQrCodePaymentModel
import com.natiqhaciyef.common.model.payment.MappedPaymentChequeModel
import com.natiqhaciyef.common.model.payment.MappedPaymentModel
import com.natiqhaciyef.common.model.payment.MappedPaymentPickModel
import com.natiqhaciyef.common.model.payment.MappedSubscriptionPlanPaymentDetails
import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState

object PaymentContract {

    sealed interface PaymentEvent : UiEvent {
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

        data class StartCamera(
            val context: Context,
            val lifecycle: LifecycleOwner,
            val preview: PreviewView,
            val onSuccess: (Any) -> Unit = { }
        ): PaymentEvent

        data class ScanQRCode(val qrCode: String, val subscriptionPlanPaymentDetails: MappedSubscriptionPlanPaymentDetails): PaymentEvent
    }

    sealed interface PaymentEffect : UiEffect {

    }

    data class PaymentState(
        override var isLoading: Boolean = false,
        var paymentMethodsList: List<MappedPaymentModel>? = null,
        var pickedPayment: MappedPaymentPickModel? = null,
        var mappedPaymentModel: MappedPaymentModel? = null,
        var cheque: MappedPaymentChequeModel? = null,
        var qrCodePaymentModel: MappedQrCodePaymentModel? = null,
        var chequePayload: String? = null,
        var paymentResult: CRUDModel? = null,
    ) : UiState
}