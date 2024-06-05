package com.natiqhaciyef.data.network.service

import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.data.network.NetworkConfig
import com.natiqhaciyef.data.network.request.PaymentModel
import com.natiqhaciyef.data.network.response.ChequePayloadModel
import com.natiqhaciyef.data.network.response.PaymentChequeModel
import com.natiqhaciyef.data.network.response.PaymentPickModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.Header
import retrofit2.http.POST
interface PaymentService {

    @POST("")
    suspend fun startPayment(
        @Header(NetworkConfig.HEADER_AUTHORIZATION) token: String,
        @Body paymentModel: PaymentModel
    ): Response<PaymentChequeModel>

    @POST("")
    suspend fun getChequePdf(
        @Header(NetworkConfig.HEADER_AUTHORIZATION) token: String,
        @Field("checkId") checkId: String
    ): Response<ChequePayloadModel>


    @POST("")
    suspend fun getAllSavedPaymentMethods(
        @Header(NetworkConfig.HEADER_AUTHORIZATION) token: String
    ): Response<List<PaymentModel>>

    @POST("")
    suspend fun getPickedPaymentDetails(
        @Header(NetworkConfig.HEADER_AUTHORIZATION) token: String,
        @Body paymentPickModel: PaymentPickModel
    ): Response<PaymentModel>

    @POST("")
    suspend fun insertNewPaymentMethod(
        @Header(NetworkConfig.HEADER_AUTHORIZATION) token: String,
        @Body paymentModel: PaymentModel
    ): Response<CRUDResponse>
}

