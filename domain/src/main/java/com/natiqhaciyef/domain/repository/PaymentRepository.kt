package com.natiqhaciyef.domain.repository

import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.repository.BaseRepository
import com.natiqhaciyef.data.local.entity.PaymentEntity
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.data.network.request.PaymentModel
import com.natiqhaciyef.data.network.response.ChequePayloadModel
import com.natiqhaciyef.data.network.response.PaymentChequeModel
import com.natiqhaciyef.data.network.response.PaymentPickModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PaymentRepository: BaseRepository {

    suspend fun startPayment(paymentModel: PaymentModel): NetworkResult<PaymentChequeModel>

    suspend fun getChequePdf(checkId: String): NetworkResult<ChequePayloadModel>

    suspend fun getAllSavedPaymentMethods(): NetworkResult<List<PaymentModel>>

    suspend fun getPickedPaymentDetails(paymentPickModel: PaymentPickModel): NetworkResult<PaymentModel>

    suspend fun insertNewPaymentMethod(paymentModel: PaymentModel): NetworkResult<CRUDResponse>


    suspend fun getStoredPaymentMethods(): List<PaymentEntity>

    suspend fun insertNewPaymentMethod(entity: PaymentEntity)

    suspend fun removePaymentMethod(details: String)
}