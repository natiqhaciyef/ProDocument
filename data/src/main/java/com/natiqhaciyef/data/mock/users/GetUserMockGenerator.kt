package com.natiqhaciyef.data.mock.users

import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.response.UserResponse

class GetUserMockGenerator(
    override var takenRequest: Unit
): BaseMockGenerator<Unit, UserResponse>() {
    override var createdMock: UserResponse = UserResponse(
        fullName = "fullname",
        phoneNumber = "+994xx xxx xx xx",
        gender = "gender",
        dateOfBirth = getNow(),
        imageUrl = "image",
        email = "email@gmail.com",
        password = "password123",
        publishDate = getNow(),
        result = CRUDResponse(
            resultCode = 299,
            message = "Mock user"
        )
    )

    override fun getMock(request: Unit, action: (Unit) -> UserResponse?): UserResponse {
        return createdMock
    }
}