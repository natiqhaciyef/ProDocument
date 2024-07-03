package com.natiqhaciyef.data.mock.users

import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.response.UserResponse

class GetUserMockGenerator(
    override var takenRequest: Unit
): BaseMockGenerator<Unit, UserResponse>() {
    override var createdMock: UserResponse =
        AccountMockManager.getUser()

    override fun getMock(request: Unit, action: (Unit) -> UserResponse?): UserResponse {
        return createdMock
    }
}