package com.natiqhaciyef.data.mock.users

import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.response.UserResponse

class GetUserMockGenerator(
    override var takenRequest: Unit
): BaseMockGenerator<Unit, UserResponse>() {
    override var createdMock: UserResponse = UserResponse(
        fullName = "Steve Wooden",
        phoneNumber = "+44 20 0001 0002",
        gender = "Male",
        dateOfBirth = "12.04.2000",
        imageUrl = "https://minecraftfaces.com/wp-content/bigfaces/big-steve-face.png",
        email = "steve@minecraft.com",
        password = "steve123",
        publishDate = "09.03.2024",
        country = "England",
        city = "Brighton",
        street = "Mr Allen street 12",
        result = CRUDResponse(
            resultCode = TWO_HUNDRED_NINETY_NINE,
            message = "Mock user"
        )
    )

    override fun getMock(request: Unit, action: (Unit) -> UserResponse?): UserResponse {
        return createdMock
    }
}