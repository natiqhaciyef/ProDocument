package com.natiqhaciyef.domain.repository

import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.repository.BaseRepository
import com.natiqhaciyef.domain.network.NetworkResult
import com.natiqhaciyef.domain.network.request.PaymentModel
import com.natiqhaciyef.domain.network.request.PaymentRequest
import com.natiqhaciyef.domain.network.request.QrCodeRequest
import com.natiqhaciyef.domain.network.response.ChequePayloadModel
import com.natiqhaciyef.domain.network.response.PaymentChequeResponse
import com.natiqhaciyef.domain.network.response.PaymentPickModel
import com.natiqhaciyef.domain.network.response.QrPaymentResponse

interface PaymentRepository: BaseRepository {

    suspend fun getPaymentData(paymentRequest: PaymentRequest): NetworkResult<PaymentChequeResponse>

    suspend fun startPayment(paymentChequeResponse: PaymentChequeResponse): NetworkResult<CRUDResponse>

    suspend fun getChequePdf(checkId: String): NetworkResult<ChequePayloadModel>

    suspend fun getAllSavedPaymentMethods(): NetworkResult<List<PaymentModel>>

    suspend fun getPickedPaymentDetails(paymentPickModel: PaymentPickModel): NetworkResult<PaymentModel>

    suspend fun insertNewPaymentMethod(paymentModel: PaymentModel): NetworkResult<CRUDResponse>

    suspend fun getPaymentHistory(): NetworkResult<List<PaymentChequeResponse>>

    suspend fun getPaymentHistoryDetails(chequeId: String): NetworkResult<PaymentChequeResponse>

    suspend fun scanQrCodePayment(qrCodeRequest: QrCodeRequest): NetworkResult<QrPaymentResponse>
}