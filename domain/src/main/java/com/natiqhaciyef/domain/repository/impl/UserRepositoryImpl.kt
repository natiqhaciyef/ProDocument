package com.natiqhaciyef.domain.repository.impl

import com.natiqhaciyef.common.model.ui.UIResult
import com.natiqhaciyef.common.model.mapped.MappedUserModel
import com.natiqhaciyef.data.local.entity.UserEntity
import com.natiqhaciyef.data.mapper.toResponse
import com.natiqhaciyef.data.mapper.toUIResult
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.data.network.response.GraphDetailsListResponse
import com.natiqhaciyef.data.source.UserDataSource
import com.natiqhaciyef.domain.repository.UserRepository

class UserRepositoryImpl(
    private val ds: UserDataSource
) : UserRepository {

    override suspend fun getUser() = ds.getUserFromNetwork()

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

    override suspend fun getUserStatics(): NetworkResult<GraphDetailsListResponse> =
        ds.getUserStatics()

    override suspend fun logout() = ds.logout()
}