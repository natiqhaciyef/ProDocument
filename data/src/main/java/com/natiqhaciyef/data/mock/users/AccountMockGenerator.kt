package com.natiqhaciyef.data.mock.users

import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.response.TokenResponse
import com.natiqhaciyef.domain.network.response.UserResponse
import java.util.UUID

class AccountMockGenerator(
    override var takenRequest: UserResponse,
) : BaseMockGenerator<UserResponse, TokenResponse>() {

    override var createdMock: TokenResponse =
        if (type == CREATE_TYPE)
            AccountMockManager.createUser(takenRequest)
        else
            AccountMockManager.updateUser(takenRequest)

    override fun getMock(
        action: ((UserResponse) -> TokenResponse?)?
    ): TokenResponse {
        if (action != null)
            try {
                return action.invoke(takenRequest) ?: throw Companion.MockRequestException(
                    MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
                )
            } catch (e: Exception) {
                println(e)
            }


        return when (type) {
            CREATE_TYPE -> AccountMockManager.createUser(takenRequest)
            UPDATE_TYPE -> AccountMockManager.updateUser(takenRequest)
            else -> return AccountMockManager.createUser(takenRequest)
        }
    }

    companion object CreateAccountMockGenerator {
        const val CREATE_TYPE = "CREATE USER"
        const val UPDATE_TYPE = "UPDATE USER"

        val customRequest = AccountMockManager.getUser()
        var type: String = CREATE_TYPE

    }
}