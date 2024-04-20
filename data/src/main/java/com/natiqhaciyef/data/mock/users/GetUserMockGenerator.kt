package com.natiqhaciyef.data.mock.users

import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.data.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.response.CRUDResponse
import com.natiqhaciyef.data.network.response.UserResponse

class GetUserMockGenerator(
    override var takenRequest: String
): BaseMockGenerator<String, UserResponse>() {
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

    override fun getMock(request: String, action: (String) -> UserResponse?): UserResponse {
        return if (request == takenRequest){
            createdMock
        }else{
            action.invoke(request) ?: throw Companion.MockRequestException()
        }
    }
}