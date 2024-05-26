package com.natiqhaciyef.data.network.service

import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.data.network.NetworkConfig
import com.natiqhaciyef.data.network.response.SubscriptionResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface SubscriptionService {

    @GET("")
    suspend fun getAllSubscriptionPlans(
        @Header(NetworkConfig.HEADER_AUTHORIZATION) token: String
    ): Response<List<SubscriptionResponse>>

    @POST("")
    suspend fun activatePickedPlan(
        @Header(NetworkConfig.HEADER_AUTHORIZATION) token: String,
        @Field("pickedPlanToken") pickedPlanToken: String
    ): Response<CRUDResponse>
}