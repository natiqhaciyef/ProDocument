package com.natiqhaciyef.data.network.service

import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.data.network.NetworkConfig
import com.natiqhaciyef.domain.network.response.GraphDetailsListResponse
import com.natiqhaciyef.domain.network.response.TokenResponse
import com.natiqhaciyef.domain.network.response.UserResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

// maybe convert them to @Body annotation which are post request type
interface UserService {

    @POST("")
    @FormUrlEncoded
    suspend fun createAccount(
        @Field("fullName") fullName: String,
        @Field("phoneNumber") phoneNumber: String,
        @Field("gender") gender: String,
        @Field("dob") dateOfBirth: String,
        @Field("imageUrl") imageUrl: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<TokenResponse>

    @GET("")
    suspend fun getUser(
        @Header(NetworkConfig.HEADER_AUTHORIZATION) token: String
    ): Response<UserResponse>

    @POST("")
    @FormUrlEncoded
    suspend fun signIn(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<TokenResponse>

    @GET("")
    suspend fun getOtp(
        @Query("email") email: String
    ): Response<CRUDResponse>

    @POST("")
    @FormUrlEncoded
    suspend fun sendOtp(
        @Field("otp") otp: String
    ): Response<CRUDResponse>

    @POST("")
    @FormUrlEncoded
    suspend fun updateUserPasswordByEmail(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<TokenResponse>

    @POST("")
    suspend fun getUserStatics(
        @Header(NetworkConfig.HEADER_AUTHORIZATION) token: String
    ): Response<GraphDetailsListResponse>

    @GET("")
    suspend fun logout(
        @Header(NetworkConfig.HEADER_AUTHORIZATION) token: String
    ): Response<CRUDResponse>
}