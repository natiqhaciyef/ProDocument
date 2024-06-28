package com.natiqhaciyef.domain.repository

import com.natiqhaciyef.core.base.repository.BaseRepository
import com.natiqhaciyef.common.model.ui.UIResult
import com.natiqhaciyef.common.model.mapped.MappedUserModel
import com.natiqhaciyef.data.local.entity.UserEntity
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.data.network.response.GraphDetailsListResponse
import com.natiqhaciyef.data.network.response.TokenResponse
import com.natiqhaciyef.data.network.response.UserResponse

interface UserRepository : BaseRepository {

    suspend fun getUser(): NetworkResult<UserResponse>

    suspend fun createAccount(user: MappedUserModel): NetworkResult<TokenResponse>

    suspend fun signIn(email: String, password: String): NetworkResult<TokenResponse>

    suspend fun getOtp(email: String): NetworkResult<CRUDResponse>

    suspend fun sendOtp(otp: String): NetworkResult<CRUDResponse>

    suspend fun updateUserPasswordByEmail(email: String, password: String): NetworkResult<TokenResponse>

    suspend fun getUserStatics(): NetworkResult<GraphDetailsListResponse>

    suspend fun logout(): NetworkResult<CRUDResponse>


    suspend fun getUserFromLocal(): List<UIResult<MappedUserModel>>?

    suspend fun insertToLocal(userEntity: UserEntity)

    suspend fun removeFromLocal(userEntity: UserEntity)

    suspend fun updateFromLocal(userEntity: UserEntity)

}