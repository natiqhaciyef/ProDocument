package com.natiqhaciyef.domain.repository.impl

import com.natiqhaciyef.common.model.payment.PaymentDetails
import com.natiqhaciyef.data.local.entity.PaymentEntity
import com.natiqhaciyef.data.network.request.PaymentModel
import com.natiqhaciyef.data.network.response.PaymentPickModel
import com.natiqhaciyef.data.source.PaymentDataSource
import com.natiqhaciyef.domain.repository.PaymentRepository

class PaymentRepositoryImpl(private val ds: PaymentDataSource) : PaymentRepository {
    override suspend fun startPayment(paymentModel: PaymentModel) =
        ds.startPayment(paymentModel)

    override suspend fun getChequePdf(checkId: String) =
        ds.getChequePdf(checkId)

    override suspend fun getAllSavedPaymentMethods() =
        ds.getAllSavedPaymentMethods()

    override suspend fun getPickedPaymentDetails(paymentPickModel: PaymentPickModel) =
        ds.getPickedPaymentDetails(paymentPickModel)

    override suspend fun insertNewPaymentMethod(paymentModel: PaymentModel) =
        ds.insertNewPaymentMethod(paymentModel)



    override suspend fun getStoredPaymentMethods(): List<PaymentEntity> =
        ds.getStoredPaymentMethods()

    override suspend fun insertNewPaymentMethod(entity: PaymentEntity) =
        ds.insertNewPaymentMethod(entity)

    override suspend fun removePaymentMethod(details: String) =
        ds.removePaymentMethod(details)
}