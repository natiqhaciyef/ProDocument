package com.natiqhaciyef.data.network

import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.domain.mapper.toMapped
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
                val storedToken = manager.getTokens()?.refreshToken ?: EMPTY_STRING
                try {
                    val updatedToken = service.updateAccessToken(storedToken)
                    manager.saveTokens(updatedToken.toMapped())

                    response.request.newBuilder()
                        .header(NetworkConfig.HEADER_AUTHORIZATION, getUpdatedAccessToken(updatedToken.accessToken ?: EMPTY_STRING))
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