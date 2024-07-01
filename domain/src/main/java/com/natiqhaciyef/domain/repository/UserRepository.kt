package com.natiqhaciyef.domain.repository

import com.natiqhaciyef.core.base.repository.BaseRepository
import com.natiqhaciyef.common.model.mapped.MappedUserModel
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.domain.network.NetworkResult
import com.natiqhaciyef.domain.network.response.GraphDetailsListResponse
import com.natiqhaciyef.domain.network.response.TokenResponse
import com.natiqhaciyef.domain.network.response.UserResponse

interface UserRepository : BaseRepository {

    suspend fun getUser(): NetworkResult<UserResponse>

    suspend fun createAccount(user: MappedUserModel): NetworkResult<TokenResponse>

    suspend fun signIn(email: String, password: String): NetworkResult<TokenResponse>

    suspend fun getOtp(email: String): NetworkResult<CRUDResponse>

    suspend fun sendOtp(otp: String): NetworkResult<CRUDResponse>

    suspend fun updateUserPasswordByEmail(email: String, password: String): NetworkResult<TokenResponse>

    suspend fun getUserStatics(): NetworkResult<GraphDetailsListResponse>

    suspend fun logout(): NetworkResult<CRUDResponse>
}