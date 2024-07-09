package com.natiqhaciyef.data.source

import com.natiqhaciyef.common.constants.OTP_MOCK_KEY
import com.natiqhaciyef.common.constants.USER_EMAIL_MOCK_KEY
import com.natiqhaciyef.common.constants.USER_PASSWORD_MOCK_KEY
import com.natiqhaciyef.common.constants.USER_TOKEN_MOCK_KEY
import com.natiqhaciyef.core.base.mock.generateMockerClass
import com.natiqhaciyef.data.mock.users.AccountMockGenerator
import com.natiqhaciyef.data.mock.users.AccountMockGenerator.CreateAccountMockGenerator.CREATE_TYPE
import com.natiqhaciyef.data.mock.users.AccountMockGenerator.CreateAccountMockGenerator.UPDATE_TYPE
import com.natiqhaciyef.data.mock.users.OtpMockGenerator
import com.natiqhaciyef.data.mock.users.GetUserMockGenerator
import com.natiqhaciyef.data.mock.users.GetUserStatisticsMockGenerator
import com.natiqhaciyef.data.mock.users.LogOutMockGenerator
import com.natiqhaciyef.data.mock.users.SignInMockGenerator
import com.natiqhaciyef.core.base.network.LoadType
import com.natiqhaciyef.core.base.network.handleNetworkResponse
import com.natiqhaciyef.data.network.manager.TokenManager
import com.natiqhaciyef.domain.network.response.UserResponse
import com.natiqhaciyef.data.network.service.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val manager: TokenManager,
    private val service: UserService
) {
    // network
    suspend fun getUserFromNetwork() = withContext(Dispatchers.IO) {
        val requestHeader = manager.generateToken()
        val mock = generateMockerClass(GetUserMockGenerator::class, Unit)
            .getMock(null)

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.getUser(requestHeader)
        }
    }

    suspend fun createAccountFromNetwork(
        userModel: UserResponse
    ) = withContext(Dispatchers.IO) {
        AccountMockGenerator.type = CREATE_TYPE
        val mock = generateMockerClass(AccountMockGenerator::class, userModel)
            .getMock(null)

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.createAccount(
                fullName = userModel.fullName,
                phoneNumber = userModel.phoneNumber,
                gender = userModel.gender,
                dateOfBirth = userModel.dateOfBirth,
                imageUrl = userModel.imageUrl,
                email = userModel.email,
                password = userModel.password
            )
        }
    }

    suspend fun signInFromNetwork(
        email: String, password: String
    ) = withContext(Dispatchers.IO) {
        val map = mapOf(USER_EMAIL_MOCK_KEY to email, USER_PASSWORD_MOCK_KEY to password)
        val mock = generateMockerClass(SignInMockGenerator::class, map)
            .getMock(null)

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.signIn(email, password)
        }
    }

    suspend fun getOtpFromNetwork(email: String) = withContext(Dispatchers.IO) {
        val mock = generateMockerClass(OtpMockGenerator::class, email)
            .getMock(null)

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.getOtp(email)
        }
    }

    suspend fun sendOtpToNetwork(otp: String) = withContext(Dispatchers.IO) {
        val mock = generateMockerClass(OtpMockGenerator::class, otp)
            .getMock(null)

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.sendOtp(otp)
        }
    }

    suspend fun updateUserPasswordByEmailFromNetwork(
        email: String,
        password: String
    ) = withContext(Dispatchers.IO) {
        AccountMockGenerator.type = UPDATE_TYPE

        val map = mapOf(USER_EMAIL_MOCK_KEY to email, USER_PASSWORD_MOCK_KEY to password)
        val mock = generateMockerClass(AccountMockGenerator::class, map)
            .getMock(null)

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.updateUserPasswordByEmail(email, password)
        }
    }

    suspend fun getUserStatics() = withContext(Dispatchers.IO){
        val requestHeader = manager.generateToken()
        val mock = generateMockerClass(GetUserStatisticsMockGenerator::class, Unit)
            .getMock(null)

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.getUserStatics(requestHeader)
        }
    }

    suspend fun logout() = withContext(Dispatchers.IO) {
        val requestHeader = manager.generateToken()
        val mock = generateMockerClass(LogOutMockGenerator::class, Unit)
            .getMock(null)
        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.logout(requestHeader)
        }
    }
}