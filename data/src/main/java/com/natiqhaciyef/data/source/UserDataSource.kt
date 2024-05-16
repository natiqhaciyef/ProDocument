package com.natiqhaciyef.data.source

import com.natiqhaciyef.common.objects.OTP_MOCK_KEY
import com.natiqhaciyef.common.objects.USER_EMAIL_MOCK_KEY
import com.natiqhaciyef.common.objects.USER_PASSWORD_MOCK_KEY
import com.natiqhaciyef.common.objects.USER_TOKEN_MOCK_KEY
import com.natiqhaciyef.core.base.mock.generateMockerClass
import com.natiqhaciyef.data.local.dao.UserDao
import com.natiqhaciyef.data.local.entity.UserEntity
import com.natiqhaciyef.data.mock.users.AccountMockGenerator
import com.natiqhaciyef.data.mock.users.OtpMockGenerator
import com.natiqhaciyef.data.mock.users.GetUserMockGenerator
import com.natiqhaciyef.data.mock.users.LogOutMockGenerator
import com.natiqhaciyef.data.mock.users.SignInMockGenerator
import com.natiqhaciyef.data.network.LoadType
import com.natiqhaciyef.data.network.handleNetworkResponse
import com.natiqhaciyef.data.network.manager.TokenManager
import com.natiqhaciyef.data.network.response.UserResponse
import com.natiqhaciyef.data.network.service.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserDataSource(
    private val manager: TokenManager,
    private val service: UserService,
    private val dao: UserDao
) {
    // network
    suspend fun getUserFromNetwork() = withContext(Dispatchers.IO) {
        val requestHeader = manager.generateToken()
        val mock = generateMockerClass(GetUserMockGenerator::class, Unit)
            .getMock(Unit) { null }

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.getUser(requestHeader)
        }
    }

    suspend fun createAccountFromNetwork(
        userModel: UserResponse
    ) = withContext(Dispatchers.IO) {
        val mock = generateMockerClass(AccountMockGenerator::class, userModel)
            .getMock(AccountMockGenerator.customRequest) { null }

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
        email: String,
        password: String
    ) = withContext(Dispatchers.IO) {
        val map = mapOf(USER_EMAIL_MOCK_KEY to email, USER_PASSWORD_MOCK_KEY to password)
        val mock = generateMockerClass(SignInMockGenerator::class, map)
            .getMock(SignInMockGenerator.customRequest) { null }

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.signIn(email, password)
        }
    }

    suspend fun getOtpFromNetwork(email: String) = withContext(Dispatchers.IO) {
        val mock = generateMockerClass(OtpMockGenerator::class, email)
            .getMock(USER_TOKEN_MOCK_KEY) { null }

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.getOtp(email)
        }
    }

    suspend fun sendOtpToNetwork(otp: String) = withContext(Dispatchers.IO) {
        val mock = generateMockerClass(OtpMockGenerator::class, otp)
            .getMock(OTP_MOCK_KEY) { null }

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.sendOtp(otp)
        }
    }

    suspend fun updateUserPasswordByEmailFromNetwork(
        email: String,
        password: String
    ) = withContext(Dispatchers.IO) {
        val map = mapOf(USER_EMAIL_MOCK_KEY to email, USER_PASSWORD_MOCK_KEY to password)
        val mock = generateMockerClass(AccountMockGenerator::class, map)
            .getMock(AccountMockGenerator.customRequest) { null }

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.updateUserPasswordByEmail(email, password)
        }
    }

    suspend fun logout() = withContext(Dispatchers.IO) {
        val requestHeader = manager.generateToken()
        val mock = generateMockerClass(LogOutMockGenerator::class, Unit)
            .getMock(Unit) { null }
        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.logout(requestHeader)
        }
    }

    // local
    suspend fun getUserFromLocal() = withContext(Dispatchers.IO) {
        dao.getAllUser()
    }

    suspend fun insertToLocal(userEntity: UserEntity) = withContext(Dispatchers.IO) {
        dao.insertUser(userEntity)
    }

    suspend fun removeFromLocal(userEntity: UserEntity) = withContext(Dispatchers.IO) {
        dao.removeUser(userEntity)
    }

    suspend fun updateFromLocal(userEntity: UserEntity) = withContext(Dispatchers.IO) {
        dao.updateUser(userEntity)
    }

}