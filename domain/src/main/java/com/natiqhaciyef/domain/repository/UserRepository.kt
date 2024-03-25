package com.natiqhaciyef.domain.repository

import com.natiqhaciyef.domain.base.BaseRepository
import com.natiqhaciyef.common.model.UIResult
import com.natiqhaciyef.common.model.mapped.MappedUserModel
import com.natiqhaciyef.data.local.entity.UserEntity
import com.natiqhaciyef.data.network.response.CRUDResponse
import com.natiqhaciyef.data.network.response.TokenResponse
import com.natiqhaciyef.data.network.response.UserResponse

interface UserRepository : BaseRepository {

    suspend fun getUser(token: String): UserResponse?

    suspend fun createAccount(user: MappedUserModel): TokenResponse?

    suspend fun signIn(email: String, password: String): TokenResponse?

    suspend fun getOtp(token: String, email: String): CRUDResponse?

    suspend fun sendOtp(otp: String): CRUDResponse?

    suspend fun updateUserPasswordByEmail(email: String, password: String): TokenResponse?

    suspend fun logout(): CRUDResponse?


    suspend fun getUserFromLocal(): List<UIResult<MappedUserModel>>?

    suspend fun insertToLocal(userEntity: UserEntity)

    suspend fun removeFromLocal(userEntity: UserEntity)

    suspend fun updateFromLocal(userEntity: UserEntity)

}