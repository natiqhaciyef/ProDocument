package com.natiqhaciyef.data.source

import com.natiqhaciyef.data.local.dao.UserDao
import com.natiqhaciyef.data.local.entity.UserEntity
import com.natiqhaciyef.data.network.response.UserResponse
import com.natiqhaciyef.data.network.service.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserDataSource(
    private val service: UserService,
    private val dao: UserDao
) {
    // network
    suspend fun getUserFromNetwork(
        token: String,
    ) = withContext(Dispatchers.IO) { service.getUser(token = token) }

    suspend fun createAccountFromNetwork(
        userModel: UserResponse
    ) = withContext(Dispatchers.IO) {
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

    suspend fun signInFromNetwork(
        email: String,
        password: String
    ) = withContext(Dispatchers.IO) { service.signIn(email, password) }

    suspend fun getOtpFromNetwork(
        email: String,
    ) = withContext(Dispatchers.IO) { service.getOtp(email) }

    suspend fun sendOtpToNetwork(
        otp: String,
    ) = withContext(Dispatchers.IO) { service.sendOtp(otp) }

    suspend fun updateUserPasswordByEmailFromNetwork(
        email: String,
        password: String
    ) = withContext(Dispatchers.IO) { service.updateUserPasswordByEmail(email, password) }

    suspend fun logout() = withContext(Dispatchers.IO) { service.logout() }

    // local
    suspend fun getUserFromLocal() = withContext(Dispatchers.IO){
         dao.getAllUser()
    }

    suspend fun insertToLocal(userEntity: UserEntity) = withContext(Dispatchers.IO){
         dao.insertUser(userEntity)
    }

    suspend fun removeFromLocal(userEntity: UserEntity) = withContext(Dispatchers.IO){
         dao.removeUser(userEntity)
    }

    suspend fun updateFromLocal(userEntity: UserEntity) = withContext(Dispatchers.IO){
         dao.updateUser(userEntity)
    }

}