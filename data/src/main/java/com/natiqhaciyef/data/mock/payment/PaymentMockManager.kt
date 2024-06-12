package com.natiqhaciyef.data.mock.payment

import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.common.model.Currency
import com.natiqhaciyef.common.model.Time
import com.natiqhaciyef.common.model.payment.PaymentDetails
import com.natiqhaciyef.common.model.payment.PaymentMethods
import com.natiqhaciyef.common.model.payment.PaymentTypes
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.data.mock.subscription.SubscriptionMockManager
import com.natiqhaciyef.data.network.request.PaymentModel
import com.natiqhaciyef.data.network.request.PaymentRequest
import com.natiqhaciyef.data.network.request.QrCodeRequest
import com.natiqhaciyef.data.network.response.ChequePayloadModel
import com.natiqhaciyef.data.network.response.PaymentChequeResponse
import com.natiqhaciyef.data.network.response.PaymentPickModel
import com.natiqhaciyef.data.network.response.QrPaymentResponse
import com.natiqhaciyef.data.network.response.SubscriptionPlanPaymentDetails
import com.natiqhaciyef.data.network.response.SubscriptionResponse
import java.util.concurrent.Flow.Subscription

object PaymentMockManager {
    private var mockQrCode = "mockQrCode"
    private var balance = 100.0
    private var paymentRequest: PaymentRequest? = null
    private var subscriptionModel: SubscriptionResponse? = null
    private val paymentsList = mutableListOf(
        PaymentModel(
            merchantId = 100,
            paymentType = PaymentTypes.CARD.name,
            paymentMethod = PaymentMethods.MASTERCARD.name,
            paymentDetails = PaymentDetails(
                cardHolder = "Anyone",
                cardNumber = "0000 1111 2222 3333",
                expireDate = "00/00",
                currency = "USD",
                cvv = "000"
            )
        ),
        PaymentModel(
            merchantId = 101,
            paymentType = PaymentTypes.CARD.name,
            paymentMethod = PaymentMethods.VISA.name,
            paymentDetails = PaymentDetails(
                cardHolder = "No one",
                cardNumber = "0000 2222 4444 6666",
                expireDate = "00/00",
                currency = "USD",
                cvv = "000"
            )
        ),
        PaymentModel(
            merchantId = 99,
            paymentType = PaymentTypes.QR.name,
            paymentMethod = PaymentMethods.GOOGLE_PAY.name,
            paymentDetails = PaymentDetails(
                cardHolder = "Anyone",
                cardNumber = "0000 9999 8888 7777",
                expireDate = "00/00",
                currency = "USD",
                cvv = "000"
            )
        ),
    )
    private val chequeList = mutableListOf<PaymentChequeResponse>()

    fun startPayment(chequeModel: PaymentChequeResponse): CRUDResponse {
        if (chequeModel.totalAmount > balance)
            return CRUDResponse(
                resultCode = 409,
                message = "mock result fail"
            )

        balance -= chequeModel.totalAmount

        return CRUDResponse(
            resultCode = 299,
            message = "mock result success"
        )
    }

    fun insertNewPayment(paymentModel: PaymentModel): CRUDResponse {
        if (!paymentsList.contains(paymentModel))
            paymentsList.add(paymentModel)
        return if (paymentsList.contains(paymentModel))
            CRUDResponse(
                resultCode = 299,
                message = "mock result success"
            )
        else
            CRUDResponse(
                resultCode = 500,
                message = "mock result fail"
            )
    }

    private fun insertCheque(chequeModel: PaymentChequeResponse): CRUDResponse {
        if (!chequeList.contains(chequeModel))
            chequeList.add(chequeModel)
        return if (chequeList.contains(chequeModel))
            CRUDResponse(
                resultCode = 299,
                message = "mock result success"
            )
        else
            CRUDResponse(
                resultCode = 500,
                message = "mock result fail"
            )
    }

    fun removePayment(paymentModel: PaymentModel) {
        paymentsList.remove(paymentModel)
    }

    fun removePayment(index: Int) {
        paymentsList.removeAt(index)
    }

    fun getPayment(payment: PaymentRequest): PaymentChequeResponse {
        val plan = SubscriptionMockManager.getAllSubscriptions()
            .find { it.token == payment.pickedPlanToken }
            ?: SubscriptionMockManager.getAllSubscriptions().first()
        subscriptionModel = plan
        paymentRequest = payment

        val price = plan.price * 12
        val fee = price * 0.05
        val paymentDetails = payment.paymentDetails

        val cheque = PaymentChequeResponse(
            checkId = "mock-key-id",
            title = "Payment for plan",
            description = "Payment refund is not available",
            subscriptionDetails = SubscriptionPlanPaymentDetails(
                expirationTime = 12,
                expirationTimeType = "Month",
                price = price,
                fee = fee,
                discount = 0.0
            ),
            totalAmount = price + fee,
            currency = paymentDetails.currency,
            paymentDetails = PaymentDetails(
                cardHolder = paymentDetails.cardHolder,
                cardNumber = paymentDetails.cardNumber,
                expireDate = paymentDetails.expireDate,
                currency = paymentDetails.currency,
                cvv = paymentDetails.cvv
            ),
            paymentResult = "SUCCESS",
            date = getNow()
        )

        insertCheque(cheque)

        return cheque
    }

    fun getAllPayment(): MutableList<PaymentModel> {
        return paymentsList
    }

    fun getAllCheques(): MutableList<PaymentChequeResponse> {
        return chequeList
    }

    fun getChequeDetails(chequeId: String): PaymentChequeResponse {
        return chequeList.find { it.checkId == chequeId } ?: chequeList.first()
    }

    fun getChequePayload(chequeId: String): ChequePayloadModel? {
        return if (chequeList.any { it.checkId == chequeId })
            ChequePayloadModel(payload = "Payload")
        else
            null
    }

    fun getPickedPaymentDetails(picked: PaymentPickModel): PaymentModel? {
        return paymentsList.find {
            val item = it.paymentDetails.cardNumber

            it.paymentType == picked.type &&
                    picked.maskedCardNumber.endsWith(
                        item.substring(
                            item.length - 5,
                            item.length - 1
                        )
                    )
        }
    }

    fun scanQrCodePayment(qrCode: QrCodeRequest): QrPaymentResponse {
        val planDetails = qrCode.subscriptionDetails
        val qrResult = QrPaymentResponse(
            merchantId = 99,
            paymentType = PaymentTypes.QR.name,
            paymentMethod = PaymentMethods.GOOGLE_PAY.name,
            cheque = PaymentChequeResponse(
                checkId = "request-qr-chque",
                title = "Beginner",
                description = "EMPTY AND VOID",
                subscriptionDetails = SubscriptionPlanPaymentDetails(
                    expirationTimeType = planDetails.expirationTimeType,
                    expirationTime = planDetails.expirationTime * 12,
                    price = planDetails.price * 12,
                    fee = planDetails.fee * 12,
                    discount = 0.0
                ),
                totalAmount = (planDetails.fee + planDetails.price) - (planDetails.fee + planDetails.price) * planDetails.discount / 100,
                currency = Currency.USD.name,
                paymentDetails = PaymentDetails(
                    cardHolder = "Anyone",
                    cardNumber = "0000 1111 2222 3333",
                    expireDate = "00/00",
                    currency = "USD",
                    cvv = "000"
                ),
                paymentResult = "SUCCESS",
                date = getNow()
            )
        )

        return if (mockQrCode == qrCode.qrCode)
            qrResult
        else
            qrResult.copy(cheque = qrResult.cheque.copy(paymentResult = "FAILED"))
    }
}