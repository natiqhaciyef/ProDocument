package com.natiqhaciyef.data.mock.users

import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.response.UserResponse

class GetUserMockGenerator(
    override var takenRequest: Unit
) : BaseMockGenerator<Unit, UserResponse>() {
    override var createdMock: UserResponse =
        AccountMockManager.getUser()

    override fun getMock(
        action: ((Unit) -> UserResponse?)?
    ): UserResponse {
        if (action != null)
            try {
                return action.invoke(takenRequest) ?: throw Companion.MockRequestException(
                    MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
                )
            } catch (e: Exception) {
                println(e)
            }

        return createdMock
    }
}