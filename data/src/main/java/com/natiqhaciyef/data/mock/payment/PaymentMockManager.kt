package com.natiqhaciyef.data.mock.payment

import com.natiqhaciyef.common.constants.FIVE
import com.natiqhaciyef.common.constants.FIVE_HUNDRED
import com.natiqhaciyef.common.constants.FOUR_HUNDRED_NINE
import com.natiqhaciyef.common.constants.HUNDRED
import com.natiqhaciyef.common.constants.HUNDRED_ONE
import com.natiqhaciyef.common.constants.NINETEEN
import com.natiqhaciyef.common.constants.NINETY_NINE
import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.constants.TWELVE
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.common.model.Currency
import com.natiqhaciyef.common.model.Time
import com.natiqhaciyef.common.model.payment.PaymentDetails
import com.natiqhaciyef.common.model.payment.PaymentMethods
import com.natiqhaciyef.common.model.payment.PaymentTypes
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.data.mock.subscription.SubscriptionMockManager
import com.natiqhaciyef.domain.network.request.PaymentModel
import com.natiqhaciyef.domain.network.request.PaymentRequest
import com.natiqhaciyef.domain.network.request.QrCodeRequest
import com.natiqhaciyef.domain.network.response.ChequePayloadModel
import com.natiqhaciyef.domain.network.response.PaymentChequeResponse
import com.natiqhaciyef.domain.network.response.PaymentPickModel
import com.natiqhaciyef.domain.network.response.QrPaymentResponse
import com.natiqhaciyef.domain.network.response.SubscriptionPlanPaymentDetails
import com.natiqhaciyef.domain.network.response.SubscriptionResponse
import java.util.concurrent.Flow.Subscription

object PaymentMockManager {
    private var crudTitle = "crud result"
    private var mockQrCode = "mockQrCode"
    private var balance = HUNDRED.toDouble()
    private var paymentRequest: PaymentRequest? = null
    private var subscriptionModel: SubscriptionResponse? = null
    private val paymentsList = mutableListOf(
        PaymentModel(
            merchantId = HUNDRED,
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
            merchantId = HUNDRED_ONE,
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
            merchantId = NINETY_NINE,
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
    private var crudResponse = CRUDResponse(
        resultCode = TWO_HUNDRED_NINETY_NINE,
        message = crudTitle
    )

    fun startPayment(chequeModel: PaymentChequeResponse): CRUDResponse {
        if (chequeModel.totalAmount > balance)
            return crudResponse.copy(
                resultCode = FOUR_HUNDRED_NINE,
                message = "$crudTitle failed"
            )

        balance -= chequeModel.totalAmount

        return crudResponse.copy(message = "$crudTitle succeed")
    }

    fun insertNewPayment(paymentModel: PaymentModel): CRUDResponse {
        if (!paymentsList.contains(paymentModel))
            paymentsList.add(paymentModel)
        return if (paymentsList.contains(paymentModel))
            crudResponse.copy(message = "$crudTitle succeed")
        else
            crudResponse.copy(
                resultCode = FIVE_HUNDRED,
                message = "$crudTitle failed"
            )
    }

    private fun insertCheque(chequeModel: PaymentChequeResponse): CRUDResponse {
        if (!chequeList.contains(chequeModel))
            chequeList.add(chequeModel)
        return if (chequeList.contains(chequeModel))
            crudResponse.copy(message = "$crudTitle succeed")
        else
            crudResponse.copy(
                resultCode = FIVE_HUNDRED,
                message = "$crudTitle failed"
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

        val price = plan.price * TWELVE
        val fee = price * 0.05
        val paymentDetails = payment.paymentDetails

        val cheque = PaymentChequeResponse(
            checkId = "mock-key-id",
            title = plan.title,
            description = "Payment refund is not available",
            subscriptionDetails = SubscriptionPlanPaymentDetails(
                expirationTime = TWELVE,
                expirationTimeType = "Month",
                price = price,
                fee = fee,
                discount = ZERO.toDouble()
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

    fun getChequePayload(chequeId: String): ChequePayloadModel {
        return if (chequeList.any { it.checkId == chequeId })
            ChequePayloadModel(payload = "Payload")
        else
            ChequePayloadModel(payload = "Failed")

    }

    fun getPickedPaymentDetails(picked: PaymentPickModel): PaymentModel {
        return paymentsList.find {
            val item = it.paymentDetails.cardNumber

            it.paymentType == picked.type &&
                    picked.maskedCardNumber.endsWith(
                        item.substring(
                            item.length - FIVE,
                            item.length - ONE
                        )
                    )
        } ?: paymentsList.first()
    }

    fun scanQrCodePayment(qrCode: QrCodeRequest): QrPaymentResponse {
        val planDetails = qrCode.subscriptionDetails
        val qrResult = QrPaymentResponse(
            merchantId = NINETY_NINE,
            paymentType = PaymentTypes.QR.name,
            paymentMethod = PaymentMethods.GOOGLE_PAY.name,
            cheque = PaymentChequeResponse(
                checkId = "request-qr-chque",
                title = "Beginner",
                description = "EMPTY AND VOID",
                subscriptionDetails = SubscriptionPlanPaymentDetails(
                    expirationTimeType = planDetails.expirationTimeType,
                    expirationTime = planDetails.expirationTime * TWELVE,
                    price = planDetails.price * TWELVE,
                    fee = planDetails.fee * TWELVE,
                    discount = ZERO.toDouble()
                ),
                totalAmount = (planDetails.fee + planDetails.price) - (planDetails.fee + planDetails.price) * planDetails.discount / HUNDRED,
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

    fun getResult() = crudResponse
}