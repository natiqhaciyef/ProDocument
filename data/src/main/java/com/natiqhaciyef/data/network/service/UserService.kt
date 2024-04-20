package com.natiqhaciyef.data.network.service

import com.natiqhaciyef.data.network.response.CRUDResponse
import com.natiqhaciyef.data.network.response.TokenResponse
import com.natiqhaciyef.data.network.response.UserResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// maybe convert them to @Body annotation which are post request type
interface UserService {

    @POST("")
    @FormUrlEncoded
    suspend fun createAccount(
        @Field("full_name") fullName: String,
        @Field("phone_number") phoneNumber: String,
        @Field("gender") gender: String,
        @Field("dob") dateOfBirth: String,
        @Field("image_url") imageUrl: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<TokenResponse>

    @GET("")
    suspend fun getUser(
        @Query("email") email: String,
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

    @GET("")
    suspend fun logout(): Response<CRUDResponse>
}