package com.natiqhaciyef.domain.repository

import com.natiqhaciyef.domain.base.repository.BaseRepository
import com.natiqhaciyef.common.model.UIResult
import com.natiqhaciyef.common.model.mapped.MappedUserModel
import com.natiqhaciyef.data.local.entity.UserEntity
import com.natiqhaciyef.data.network.response.CRUDResponse
import com.natiqhaciyef.data.network.response.TokenResponse
import com.natiqhaciyef.data.network.response.UserResponse

interface UserRepository : BaseRepository {

    suspend fun getUser(token: String): Result<UserResponse>

    suspend fun createAccount(user: MappedUserModel): Result<TokenResponse>

    suspend fun signIn(email: String, password: String): Result<TokenResponse>

    suspend fun getOtp(email: String): Result<CRUDResponse>

    suspend fun sendOtp(otp: String): Result<CRUDResponse>

    suspend fun updateUserPasswordByEmail(email: String, password: String): Result<TokenResponse>

    suspend fun logout(): Result<CRUDResponse>


    suspend fun getUserFromLocal(): List<UIResult<MappedUserModel>>?

    suspend fun insertToLocal(userEntity: UserEntity)

    suspend fun removeFromLocal(userEntity: UserEntity)

    suspend fun updateFromLocal(userEntity: UserEntity)

}