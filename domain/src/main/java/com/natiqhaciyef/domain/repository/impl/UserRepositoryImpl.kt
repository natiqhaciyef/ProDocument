package com.natiqhaciyef.domain.repository.impl

import com.natiqhaciyef.domain.mapper.toResponse
import com.natiqhaciyef.domain.mapper.toUIResult
import com.natiqhaciyef.common.model.UIResult
import com.natiqhaciyef.common.model.mapped.MappedUserModel
import com.natiqhaciyef.data.local.entity.UserEntity
import com.natiqhaciyef.data.network.response.CRUDResponse
import com.natiqhaciyef.data.network.response.TokenResponse
import com.natiqhaciyef.data.network.response.UserResponse
import com.natiqhaciyef.data.source.UserDataSource
import com.natiqhaciyef.domain.repository.UserRepository

class UserRepositoryImpl(
    private val ds: UserDataSource
) : UserRepository {

    override suspend fun getUser(
        token: String,
    ) = ds.getUserFromNetwork(token = token)

    override suspend fun createAccount(user: MappedUserModel) =
        ds.createAccountFromNetwork(userModel = user.toResponse())

    override suspend fun signIn(email: String, password: String) =
        ds.signInFromNetwork(email, password)

    override suspend fun getOtp(email: String) = ds.getOtpFromNetwork(email)

    override suspend fun sendOtp(otp: String) = ds.sendOtpToNetwork(otp)

    override suspend fun updateUserPasswordByEmail(
        email: String,
        password: String
    ) = ds.updateUserPasswordByEmailFromNetwork(email, password)

    override suspend fun logout() = ds.logout()


    override suspend fun getUserFromLocal(): List<UIResult<MappedUserModel>>? =
        ds.getUserFromLocal()?.map { entity -> entity.toUIResult() }

    override suspend fun insertToLocal(userEntity: UserEntity) =
        ds.insertToLocal(userEntity)

    override suspend fun removeFromLocal(userEntity: UserEntity) =
        ds.removeFromLocal(userEntity)

    override suspend fun updateFromLocal(userEntity: UserEntity) =
        ds.updateFromLocal(userEntity)

}