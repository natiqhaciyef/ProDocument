package com.natiqhaciyef.data.impl

import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.domain.network.NetworkResult
import com.natiqhaciyef.domain.network.request.PaymentModel
import com.natiqhaciyef.domain.network.request.PaymentRequest
import com.natiqhaciyef.domain.network.request.QrCodeRequest
import com.natiqhaciyef.domain.network.response.PaymentChequeResponse
import com.natiqhaciyef.domain.network.response.PaymentPickModel
import com.natiqhaciyef.domain.network.response.QrPaymentResponse
import com.natiqhaciyef.data.source.PaymentDataSource
import com.natiqhaciyef.domain.repository.PaymentRepository
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(val ds: PaymentDataSource) : PaymentRepository {
    override suspend fun getPaymentData(paymentRequest: PaymentRequest) =
        ds.getPaymentData(paymentRequest)

    override suspend fun startPayment(paymentChequeResponse: PaymentChequeResponse): NetworkResult<CRUDResponse> {
        return ds.startPayment(paymentChequeResponse)
    }

    override suspend fun getChequePdf(checkId: String) =
        ds.getChequePdf(checkId)

    override suspend fun getAllSavedPaymentMethods() =
        ds.getAllSavedPaymentMethods()

    override suspend fun getPickedPaymentDetails(paymentPickModel: PaymentPickModel) =
        ds.getPickedPaymentDetails(paymentPickModel)

    override suspend fun insertNewPaymentMethod(paymentModel: PaymentModel) =
        ds.insertNewPaymentMethod(paymentModel)

    override suspend fun getPaymentHistory(): NetworkResult<List<PaymentChequeResponse>> =
        ds.getPaymentHistory()

    override suspend fun getPaymentHistoryDetails(chequeId: String): NetworkResult<PaymentChequeResponse> =
        ds.getPaymentHistoryDetails(chequeId)

    override suspend fun scanQrCodePayment(qrCodeRequest: QrCodeRequest): NetworkResult<QrPaymentResponse> =
        ds.scanQrCodePayment(qrCodeRequest)

}