package com.natiqhaciyef.prodocument.domain.repository.impl

import com.natiqhaciyef.prodocument.common.mapper.toUIResult
import com.natiqhaciyef.prodocument.data.local.entity.UserEntity
import com.natiqhaciyef.prodocument.data.model.UserModel
import com.natiqhaciyef.prodocument.data.network.response.CRUDResponse
import com.natiqhaciyef.prodocument.data.network.response.TokenResponse
import com.natiqhaciyef.prodocument.data.network.response.UserResponse
import com.natiqhaciyef.prodocument.data.source.UserDataSource
import com.natiqhaciyef.prodocument.domain.model.UIResult
import com.natiqhaciyef.prodocument.domain.model.mapped.MappedUserModel
import com.natiqhaciyef.prodocument.domain.repository.UserRepository

class UserRepositoryImpl(
    private val ds: UserDataSource
) : UserRepository {

    override suspend fun getUser(
        token: String,
    ): UserResponse? =
        ds.getUserFromNetwork(token = token)

    override suspend fun createAccount(user: UserModel): TokenResponse? =
        ds.createAccountFromNetwork(userModel = user)

    override suspend fun signIn(email: String, password: String): TokenResponse? =
        ds.signInFromNetwork(email, password)

    override suspend fun getOtp(email: String): CRUDResponse? =
        ds.getOtpFromNetwork(email)

    override suspend fun sendOtp(otp: String): CRUDResponse? =
        ds.sendOtpToNetwork(otp)

    override suspend fun updateUserPasswordByEmail(
        email: String,
        password: String
    ): CRUDResponse? = ds.updateUserPasswordByEmailFromNetwork(email, password)


    override suspend fun getUserFromLocal(): List<UIResult<MappedUserModel>>? =
        ds.getUserFromLocal()?.map { entity -> entity.toUIResult() }

    override suspend fun insertToLocal(userEntity: UserEntity) =
        ds.insertToLocal(userEntity)

    override suspend fun removeFromLocal(userEntity: UserEntity) =
        ds.removeFromLocal(userEntity)

    override suspend fun updateFromLocal(userEntity: UserEntity) =
        ds.updateFromLocal(userEntity)

}