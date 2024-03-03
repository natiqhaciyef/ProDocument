package com.natiqhaciyef.prodocument.data.network.service

import com.natiqhaciyef.prodocument.data.network.response.MaterialResponse
import com.natiqhaciyef.prodocument.data.network.response.MaterialsResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MaterialService {

    @POST("")
    @FormUrlEncoded
    suspend fun getMaterials(
        @Field("token") token: String
    ): MaterialsResponse?

    @POST("")
    @FormUrlEncoded
    suspend fun getMaterialById(
        @Field("id") materialId: String,
        @Field("token") token: String
    ): MaterialResponse?


}