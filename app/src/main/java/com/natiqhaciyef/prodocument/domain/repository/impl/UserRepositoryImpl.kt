package com.natiqhaciyef.prodocument.domain.repository.impl

import android.service.autofill.UserData
import com.natiqhaciyef.prodocument.common.mapper.toUIResult
import com.natiqhaciyef.prodocument.data.model.UserIOModel
import com.natiqhaciyef.prodocument.data.network.response.TokenResponse
import com.natiqhaciyef.prodocument.data.network.response.UserResponse
import com.natiqhaciyef.prodocument.data.source.UserDataSource
import com.natiqhaciyef.prodocument.domain.model.UIResult
import com.natiqhaciyef.prodocument.domain.model.mapped.MappedUserModel
import com.natiqhaciyef.prodocument.domain.repository.UserRepository

class UserRepositoryImpl(
    val ds: UserDataSource
) : UserRepository {
    override suspend fun getUser(
        token: String,
        email: String,
        password: String
    ): UIResult<MappedUserModel>? =
        ds.getUser(token = token, email = email, password = password)?.toUIResult()


    override suspend fun createAccount(user: UserIOModel): TokenResponse? =
        ds.createAccount(userModel = user)

}