package com.natiqhaciyef.data.mock.users

import com.natiqhaciyef.common.objects.USER_EMAIL_MOCK_KEY
import com.natiqhaciyef.common.objects.USER_PASSWORD_MOCK_KEY
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.response.TokenResponse
import java.util.UUID

class SignInMockGenerator(
    override var takenRequest: Map<String, String>
) : BaseMockGenerator<Map<String, String>, TokenResponse>() {
    override var createdMock: TokenResponse = TokenResponse(
        accessToken = "${UUID.randomUUID()}",
        result = CRUDResponse(
            resultCode = 299,
            message = "Mock token"
        ),
        refreshToken = "email@gmail.com"
    )

    override fun getMock(
        request: Map<String, String>,
        action: (Map<String, String>) -> TokenResponse?
    ): TokenResponse {
        val email = request[USER_EMAIL_MOCK_KEY]
        val password = request[USER_PASSWORD_MOCK_KEY]

        return if (email == takenRequest[USER_EMAIL_MOCK_KEY] && password == takenRequest[USER_PASSWORD_MOCK_KEY]) {
            createdMock
        } else {
            action.invoke(request) ?: throw Companion.MockRequestException()
        }
    }


    companion object SignInMockGenerator {
        val customRequest = mapOf(
            USER_EMAIL_MOCK_KEY to USER_EMAIL_MOCK_KEY,
            USER_PASSWORD_MOCK_KEY to USER_PASSWORD_MOCK_KEY
        )
    }
}