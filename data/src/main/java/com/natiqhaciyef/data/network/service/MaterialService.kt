package com.natiqhaciyef.data.network.service

import com.natiqhaciyef.data.network.response.CRUDResponse
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

    @POST("")
    @FormUrlEncoded
    suspend fun createMaterialByToken(
        @Field("material_token") token: String,
        @Field("material_id") id: String,
        @Field("publish_date") publishDate: String,
        @Field("image") image: String,
        @Field("title") title: String,
        @Field("description") description: String,
        @Field("type") type: String,
        @Field("url") url: String,
    ): CRUDResponse?

    @POST("")
    @FormUrlEncoded
    suspend fun removeMaterialByToken(
        @Field("material_token") token: String,
        @Field("material_id") id: String,
    ): CRUDResponse?

    @POST("")
    @FormUrlEncoded
    suspend fun updateMaterialByToken(
        @Field("material_token") token: String,
        @Field("material_id") id: String,
        @Field("publish_date") publishDate: String,
        @Field("image") image: String,
        @Field("title") title: String,
        @Field("description") description: String,
        @Field("type") type: String,
        @Field("url") url: String,
    ): CRUDResponse?

}