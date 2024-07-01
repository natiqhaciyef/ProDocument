package com.natiqhaciyef.data.network.service

import com.natiqhaciyef.domain.network.response.TokenResponse
import retrofit2.http.POST

interface TokenService {

    @POST("")
    suspend fun updateRefreshToken(): TokenResponse

    @POST("")
    suspend fun updateAccessToken(token: String): TokenResponse
}