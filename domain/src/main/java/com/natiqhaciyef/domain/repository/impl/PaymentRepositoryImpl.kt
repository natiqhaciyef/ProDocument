package com.natiqhaciyef.domain.repository.impl

import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.data.local.entity.PaymentEntity
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.data.network.request.PaymentModel
import com.natiqhaciyef.data.network.request.PaymentRequest
import com.natiqhaciyef.data.network.request.QrCodeRequest
import com.natiqhaciyef.data.network.response.PaymentChequeResponse
import com.natiqhaciyef.data.network.response.PaymentPickModel
import com.natiqhaciyef.data.network.response.QrPaymentResponse
import com.natiqhaciyef.data.source.PaymentDataSource
import com.natiqhaciyef.domain.repository.PaymentRepository

class PaymentRepositoryImpl(private val ds: PaymentDataSource) : PaymentRepository {
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

    override suspend fun getStoredPaymentMethods(): List<PaymentEntity> =
        ds.getStoredPaymentMethods()

    override suspend fun insertNewPaymentMethod(entity: PaymentEntity) =
        ds.insertNewPaymentMethod(entity)

    override suspend fun removePaymentMethod(details: String) =
        ds.removePaymentMethod(details)
}