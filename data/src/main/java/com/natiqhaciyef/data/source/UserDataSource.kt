package com.natiqhaciyef.data.source

import com.natiqhaciyef.data.local.dao.UserDao
import com.natiqhaciyef.data.local.entity.UserEntity
import com.natiqhaciyef.data.model.UserModel
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
        userModel: UserModel
    ) = service.createAccount(
        fullName = userModel.name,
        phoneNumber = userModel.phoneNumber,
        gender = userModel.gender,
        dateOfBirth = userModel.birthDate,
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


    // local
    suspend fun getUserFromLocal() = dao.getAllUser()

    suspend fun insertToLocal(userEntity: UserEntity) = dao.insertUser(userEntity)

    suspend fun removeFromLocal(userEntity: UserEntity) = dao.removeUser(userEntity)

    suspend fun updateFromLocal(userEntity: UserEntity) = dao.updateUser(userEntity)

}