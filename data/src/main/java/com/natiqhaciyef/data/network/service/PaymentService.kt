package com.natiqhaciyef.data.network.service

import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.data.network.NetworkConfig
import com.natiqhaciyef.data.network.request.PaymentModel
import com.natiqhaciyef.data.network.request.PaymentRequest
import com.natiqhaciyef.data.network.request.QrCodeRequest
import com.natiqhaciyef.data.network.response.ChequePayloadModel
import com.natiqhaciyef.data.network.response.PaymentChequeResponse
import com.natiqhaciyef.data.network.response.PaymentPickModel
import com.natiqhaciyef.data.network.response.QrPaymentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
interface PaymentService {

    @POST("")
    suspend fun getPaymentData(
        @Header(NetworkConfig.HEADER_AUTHORIZATION) token: String,
        @Body paymentData: PaymentRequest
    ): Response<PaymentChequeResponse>

    @POST("")
    suspend fun startPayment(
        @Header(NetworkConfig.HEADER_AUTHORIZATION) token: String,
        @Body cheque: PaymentChequeResponse
    ): Response<CRUDResponse>

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
        @Body pickedPayment: PaymentPickModel
    ): Response<PaymentModel>

    @POST("")
    suspend fun insertNewPaymentMethod(
        @Header(NetworkConfig.HEADER_AUTHORIZATION) token: String,
        @Body payment: PaymentModel
    ): Response<CRUDResponse>

    @POST("")
    suspend fun getPaymentHistory(
        @Header(NetworkConfig.HEADER_AUTHORIZATION) token: String,
    ): Response<List<PaymentChequeResponse>>

    @POST("")
    suspend fun getPaymentHistoryDetails(
        @Header(NetworkConfig.HEADER_AUTHORIZATION) token: String,
        @Body chequeId: String
    ): Response<PaymentChequeResponse>

    @POST("")
    suspend fun scanQrCodePayment(
        @Header(NetworkConfig.HEADER_AUTHORIZATION) token: String,
        @Body qrCodeRequest: QrCodeRequest
    ): Response<QrPaymentResponse>
}

