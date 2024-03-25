package com.natiqhaciyef.data.network.service

import com.natiqhaciyef.data.network.response.CRUDResponse
import com.natiqhaciyef.data.network.response.TokenResponse
import com.natiqhaciyef.data.network.response.UserResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

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
    ): TokenResponse?

    @GET("")
    suspend fun getUser(
        @Query("token") token: String,
    ): UserResponse?

    @GET("")
    suspend fun signIn(
        @Query("email") email: String,
        @Query("password") password: String,
    ): TokenResponse?

    @POST("")
    @FormUrlEncoded
    suspend fun getOtp(
        @Field("token") token: String,
        @Field("email") email: String
    ): CRUDResponse?

    @POST("")
    @FormUrlEncoded
    suspend fun sendOtp(
        @Field("otp") otp: String
    ): CRUDResponse?

    @POST("")
    @FormUrlEncoded
    suspend fun updateUserPasswordByEmail(
        @Field("email") email: String,
        @Field("password") password: String,
    ): TokenResponse?

    @GET("")
    suspend fun logout(): CRUDResponse?
}