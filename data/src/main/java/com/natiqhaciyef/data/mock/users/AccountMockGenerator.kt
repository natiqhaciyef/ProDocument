package com.natiqhaciyef.data.mock.users

import com.natiqhaciyef.data.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.response.CRUDResponse
import com.natiqhaciyef.data.network.response.TokenResponse
import com.natiqhaciyef.data.network.response.UserResponse
import java.util.UUID

class AccountMockGenerator(
    override var takenRequest: UserResponse
) : BaseMockGenerator<UserResponse, TokenResponse>() {
    override var createdMock: TokenResponse =
        TokenResponse(
            uid = "${UUID.randomUUID()}",
            premiumToken = null,
            premiumTokenExpiryDate = null,
            result = CRUDResponse(
                resultCode = 299,
                message = "Mock token"
            )
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
            fullName = "fullname",
            phoneNumber = "+994xx xxx xx xx",
            gender = "gender",
            dateOfBirth = "00.00.0000",
            imageUrl = "image",
            email = "email@gmail.com",
            password = "password123",
            publishDate = "00.00.0000",
            result = null
        )
    }
}