package com.natiqhaciyef.data.network

import com.natiqhaciyef.data.mapper.toMapped
import com.natiqhaciyef.data.network.manager.TokenManager
import com.natiqhaciyef.data.network.service.TokenService
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val service: TokenService,
    private val manager: TokenManager
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        synchronized(this) {
            return runBlocking {
                val storedToken = manager.getTokens()?.refreshToken ?: ""
                try {
                    val updatedToken = service.updateAccessToken(storedToken)
                    manager.saveTokens(updatedToken.toMapped())

                    response.request.newBuilder()
                        .header(NetworkConfig.HEADER_AUTHORIZATION, getUpdatedAccessToken(updatedToken.accessToken ?: ""))
                        .build()
                }catch (e: Exception){
                    manager.removeToken()
                    throw e
                }
            }
        }
    }

    private fun getUpdatedAccessToken(refreshToken: String): String {
        return NetworkConfig.HEADER_AUTHORIZATION_TYPE + refreshToken
    }
}