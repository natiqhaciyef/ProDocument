package com.natiqhaciyef.data.mock.users

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
        request: UserResponse,
        action: (UserResponse) -> TokenResponse?
    ): TokenResponse {
        if (type == CREATE_TYPE)
            AccountMockManager.createUser(takenRequest)
        else if(type == UPDATE_TYPE)
            AccountMockManager.updateUser(takenRequest)


        return AccountMockManager.createUser(takenRequest)
    }

    companion object CreateAccountMockGenerator {
        const val CREATE_TYPE = "CREATE USER"
        const val UPDATE_TYPE = "UPDATE USER"

        val customRequest = AccountMockManager.getUser()
        var type: String = CREATE_TYPE

    }
}