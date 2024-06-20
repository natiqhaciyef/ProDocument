package com.natiqhaciyef.data.mock.users

import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.response.TokenResponse
import com.natiqhaciyef.data.network.response.UserResponse
import java.util.UUID

class AccountMockGenerator(
    override var takenRequest: UserResponse
) : BaseMockGenerator<UserResponse, TokenResponse>() {
    override var createdMock: TokenResponse =
        TokenResponse(
            accessToken = "${UUID.randomUUID()}",
            result = CRUDResponse(
                resultCode = 299,
                message = "Mock token"
            ),
            refreshToken = "email@gmail.com"
        )

    override fun getMock(
        request: UserResponse,
        action: (UserResponse) -> TokenResponse?
    ): TokenResponse {
        return if (request == takenRequest){
            createdMock
        }else{
            action.invoke(request) ?: throw Companion.MockRequestException()
        }
    }


    companion object CreateAccountMockGenerator{
        val customRequest = UserResponse(
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
            result = null
        )
    }
}