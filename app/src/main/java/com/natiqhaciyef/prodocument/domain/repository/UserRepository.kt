package com.natiqhaciyef.prodocument.domain.repository

import com.natiqhaciyef.prodocument.data.model.UserIOModel
import com.natiqhaciyef.prodocument.data.network.response.TokenResponse
import com.natiqhaciyef.prodocument.data.network.response.UserResponse
import com.natiqhaciyef.prodocument.domain.model.UIResult
import com.natiqhaciyef.prodocument.domain.model.mapped.MappedUserModel

interface UserRepository {

    suspend fun getUser(token: String, email: String, password: String): UIResult<MappedUserModel>?

    suspend fun createAccount(user: UserIOModel): TokenResponse?

}