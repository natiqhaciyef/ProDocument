package com.natiqhaciyef.data.network.service

import com.natiqhaciyef.data.network.response.CRUDResponse
import com.natiqhaciyef.data.network.response.MaterialResponse
import com.natiqhaciyef.data.network.response.ListMaterialResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MaterialService {

    @GET("")
    suspend fun getMaterials(
        @Path("material_token") token: String
    ): Response<ListMaterialResponse>

    @GET("")
    suspend fun getMaterialById(
        @Query("id") materialId: String,
        @Path("material_token") token: String
    ): Response<MaterialResponse>

    @POST("")
    @FormUrlEncoded
    suspend fun createMaterialById(
        @Field("material_token") token: String,
        @Field("publish_date") publishDate: String,
        @Field("image") image: String,
        @Field("title") title: String,
        @Field("description") description: String,
        @Field("type") type: String,
        @Field("url") url: String,
    ): Response<CRUDResponse>

    @POST("")
    @FormUrlEncoded
    suspend fun removeMaterialById(
        @Field("material_token") token: String,
        @Field("material_id") id: String,
    ): Response<CRUDResponse>

    @POST("")
    @FormUrlEncoded
    suspend fun updateMaterialById(
        @Field("material_token") token: String,
        @Field("material_id") id: String,
        @Field("publish_date") publishDate: String,
        @Field("image") image: String,
        @Field("title") title: String,
        @Field("description") description: String,
        @Field("type") type: String,
        @Field("url") url: String,
    ): Response<CRUDResponse>

    @POST("")
    @FormUrlEncoded
    suspend fun mergeMaterials(
        @Body materials: List<MaterialResponse>
    ): Response<MaterialResponse>
}