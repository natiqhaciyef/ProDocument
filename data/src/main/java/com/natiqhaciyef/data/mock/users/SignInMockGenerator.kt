package com.natiqhaciyef.data.mock.users

import com.natiqhaciyef.common.constants.USER_EMAIL_MOCK_KEY
import com.natiqhaciyef.common.constants.USER_PASSWORD_MOCK_KEY
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.response.TokenResponse

class SignInMockGenerator(
    override var takenRequest: Map<String, String>
) : BaseMockGenerator<Map<String, String>, TokenResponse>() {
    override var createdMock: TokenResponse =
        AccountMockManager.signIn(
            takenRequest[USER_EMAIL_MOCK_KEY].toString(),
            takenRequest[USER_PASSWORD_MOCK_KEY].toString()
        )

    override fun getMock(
        request: Map<String, String>,
        action: (Map<String, String>) -> TokenResponse?
    ): TokenResponse {

        return AccountMockManager.signIn(
            takenRequest[USER_EMAIL_MOCK_KEY].toString(),
            takenRequest[USER_PASSWORD_MOCK_KEY].toString()
        )
    }


    companion object SignInMockGenerator {
        val customRequest = mapOf(
            USER_EMAIL_MOCK_KEY to USER_EMAIL_MOCK_KEY,
            USER_PASSWORD_MOCK_KEY to USER_PASSWORD_MOCK_KEY
        )
    }
}