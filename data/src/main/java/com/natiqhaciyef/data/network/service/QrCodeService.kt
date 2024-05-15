package com.natiqhaciyef.data.network.service

import com.natiqhaciyef.data.network.NetworkConfig
import com.natiqhaciyef.data.network.response.QrCodeResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface QrCodeService {
    
    @POST("")
    @FormUrlEncoded
    suspend fun readQrCodeResult(
        @Header(NetworkConfig.HEADER_AUTHORIZATION) token: String,
        @Field("qr_code") qrCode: String
    ): Response<QrCodeResponse>
}