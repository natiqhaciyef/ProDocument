package com.natiqhaciyef.data.mock.users

import com.natiqhaciyef.common.constants.FOUR_HUNDRED_FOUR
import com.natiqhaciyef.common.constants.FOUR_HUNDRED_ONE
import com.natiqhaciyef.common.constants.FOUR_HUNDRED_SIX
import com.natiqhaciyef.common.constants.OTP_REQUEST_FAILED
import com.natiqhaciyef.common.constants.SPACE
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.common.constants.USER_NOT_FOUND
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.domain.network.response.GraphDetailModel
import com.natiqhaciyef.domain.network.response.GraphDetailsListResponse
import com.natiqhaciyef.domain.network.response.TokenResponse
import com.natiqhaciyef.domain.network.response.UserResponse
import java.util.UUID


object AccountMockManager {
    private const val CRUD_MOCK = "crud mock"
    private const val ACCESS_TOKEN_MOCK = "AlkapP11jd2oD99OlmLAp25T4PP32IFdO"
    const val EMAIL_MOCK = "steve@minecraft.com"
    private const val OTP_MOCK = "1111"

    private const val CUSTOM_TOKEN = "custom token"
    private val account = UserResponse(
        fullName = "Steve Wooden",
        phoneNumber = "+44 20 0001 0002",
        gender = "Male",
        dateOfBirth = "12.04.2000",
        imageUrl = "https://minecraftfaces.com/wp-content/bigfaces/big-steve-face.png",
        email = EMAIL_MOCK,
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

    private val crudMock = CRUDResponse(
        resultCode = TWO_HUNDRED_NINETY_NINE,
        message = CRUD_MOCK
    )

    private val mockToken = TokenResponse(
        accessToken = ACCESS_TOKEN_MOCK,
        result = crudMock,
        refreshToken = EMAIL_MOCK
    )

    private var userList = mutableListOf(Pair(account, mockToken))
    private val userStats = listOf(
        Pair(
            EMAIL_MOCK, GraphDetailsListResponse(
                details = listOf(
                    GraphDetailModel(
                        title = "Watermark",
                        type = "watermark",
                        percentage = 12.0,
                        backgroundColor = "brown"
                    ),
                    GraphDetailModel(
                        title = "Scan",
                        type = "scan",
                        percentage = 60.0,
                        backgroundColor = "orange"
                    ),
                    GraphDetailModel(
                        title = "Protect",
                        type = "protect",
                        percentage = 28.0,
                        backgroundColor = "green"
                    )
                )
            )
        )
    )

    fun getUser(): UserResponse {
        return account
    }

    private fun getTokenOfUser(userResponse: UserResponse): TokenResponse {
        if (userResponse == account)
            return mockToken

        val generateToken = mockToken.copy(
            accessToken = "${UUID.randomUUID()}",
            refreshToken = userResponse.email,
            result = crudMock.copy(message = CRUD_MOCK + SPACE + CUSTOM_TOKEN)
        )

        return generateToken
    }

    fun getStatistics(email: String): GraphDetailsListResponse {
        return userStats.find { it.first == email }!!.second
    }

    fun otpCheck(otp: String): CRUDResponse {
        if (otp == OTP_MOCK)
            return crudMock

        return crudMock.copy(
            resultCode = FOUR_HUNDRED_SIX,
            message = OTP_REQUEST_FAILED
        )
    }

    fun signIn(email: String, password: String): TokenResponse {
        val userPair = userList.find {
            it.first.email == email
        // && it.first.password == password
        }

        println(userPair)

        return userPair?.second!!
    }

    fun createUser(user: UserResponse): TokenResponse {
        val token = getTokenOfUser(user)
        userList.add(Pair(user, token))

        return token
    }

    fun updateUser(user: UserResponse): TokenResponse {
        val token = getTokenOfUser(user)
        val currentUser = userList.find { it.first.email == user.email }
        val index = userList.indexOf(currentUser)
        userList[index] = Pair(user, token)

        return token
    }

    fun logout(email: String): CRUDResponse {
        if (account.email == email)
            return crudMock

        return crudMock.copy(resultCode = FOUR_HUNDRED_FOUR, message = USER_NOT_FOUND)
    }
}