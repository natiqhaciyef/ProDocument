package com.natiqhaciyef.data.source

import com.natiqhaciyef.data.local.dao.UserDao
import com.natiqhaciyef.data.local.entity.UserEntity
import com.natiqhaciyef.data.network.response.UserResponse
import com.natiqhaciyef.data.network.service.UserService

class UserDataSource(
    private val service: UserService,
    private val dao: UserDao
) {
    // network
    suspend fun getUserFromNetwork(
        token: String,
    ) = service.getUser(
        token = token,
    )

    suspend fun createAccountFromNetwork(
        userModel: UserResponse
    ) = service.createAccount(
        fullName = userModel.fullName,
        phoneNumber = userModel.phoneNumber,
        gender = userModel.gender,
        dateOfBirth = userModel.dateOfBirth,
        imageUrl = userModel.imageUrl,
        email = userModel.email,
        password = userModel.password
    )

    suspend fun signInFromNetwork(
        email: String,
        password: String
    ) = service.signIn(email, password)

    suspend fun getOtpFromNetwork(
        email: String,
    ) = service.getOtp(email)

    suspend fun sendOtpToNetwork(
        otp: String,
    ) = service.sendOtp(otp)

    suspend fun updateUserPasswordByEmailFromNetwork(
        email: String,
        password: String
    ) = service.updateUserPasswordByEmail(email, password)

    suspend fun logout() = service.logout()

    // local
    suspend fun getUserFromLocal() = dao.getAllUser()

    suspend fun insertToLocal(userEntity: UserEntity) = dao.insertUser(userEntity)

    suspend fun removeFromLocal(userEntity: UserEntity) = dao.removeUser(userEntity)

    suspend fun updateFromLocal(userEntity: UserEntity) = dao.updateUser(userEntity)

}