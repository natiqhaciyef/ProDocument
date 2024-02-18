package com.natiqhaciyef.prodocument.data.network.service

import com.natiqhaciyef.prodocument.data.network.response.CRUDResponse
import com.natiqhaciyef.prodocument.data.network.response.TokenResponse
import com.natiqhaciyef.prodocument.data.network.response.UserResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserService {

    @POST("")
    @FormUrlEncoded
    suspend fun createAccount(
        @Field("fullName") fullName: String,
        @Field("phone_number") phoneNumber: String,
        @Field("gender") gender: String,
        @Field("dob") dateOfBirth: String,
        @Field("imageUrl") imageUrl: String,
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
}