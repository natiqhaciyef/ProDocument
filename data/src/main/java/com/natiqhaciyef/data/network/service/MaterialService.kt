package com.natiqhaciyef.data.network.service

import com.natiqhaciyef.data.network.request.MergeRequest
import com.natiqhaciyef.data.network.request.SplitRequest
import com.natiqhaciyef.data.network.request.WatermarkRequest
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.data.network.NetworkConfig
import com.natiqhaciyef.data.network.response.MaterialResponse
import com.natiqhaciyef.data.network.response.ListMaterialResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface
MaterialService {

    @GET("")
    suspend fun getMaterials(
        @Header(NetworkConfig.HEADER_AUTHORIZATION) token: String,
    ): Response<ListMaterialResponse>

    @GET("")
    suspend fun getMaterialById(
        @Query("id") materialId: String,
        @Path("email") email: String
    ): Response<MaterialResponse>

    @POST("")
    @FormUrlEncoded
    suspend fun createMaterialById(
        @Field("email") email: String,
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
        @Field("email") email: String,
        @Field("material_id") id: String,
    ): Response<CRUDResponse>

    @POST("")
    @FormUrlEncoded
    suspend fun updateMaterialById(
        @Field("email") email: String,
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
        @Body data: MergeRequest
    ): Response<MaterialResponse>

    @POST("")
    @FormUrlEncoded
    suspend fun watermarkMaterial(
        @Body data: WatermarkRequest
    ): Response<MaterialResponse>

    @POST("")
    @FormUrlEncoded
    suspend fun splitMaterial(
        @Body data: SplitRequest
    ): Response<List<MaterialResponse>>
}