package com.natiqhaciyef.data.network.service

import com.natiqhaciyef.data.network.response.CRUDResponse
import com.natiqhaciyef.data.network.response.TokenResponse
import com.natiqhaciyef.data.network.response.UserResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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

    @POST("")
    @FormUrlEncoded
    suspend fun getUser(
        @Field("token") token: String,
    ): UserResponse?

    @POST("")
    @FormUrlEncoded
    suspend fun signIn(
        @Field("email") email: String,
        @Field("password") password: String,
    ): TokenResponse?

    @POST("")
    @FormUrlEncoded
    suspend fun getOtp(
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
}