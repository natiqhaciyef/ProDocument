package com.natiqhaciyef.data.network.service

import com.natiqhaciyef.data.network.response.MaterialResponse
import com.natiqhaciyef.data.network.response.ListMaterialResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MaterialService {

    @POST("")
    @FormUrlEncoded
    suspend fun getMaterials(
        @Field("token") token: String
    ): ListMaterialResponse?

    @POST("")
    @FormUrlEncoded
    suspend fun getMaterialById(
        @Field("id") materialId: String,
        @Field("token") token: String
    ): MaterialResponse?


}