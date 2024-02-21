package com.natiqhaciyef.prodocument.domain.repository

import android.media.session.MediaSession.Token
import com.natiqhaciyef.prodocument.data.local.entity.UserEntity
import com.natiqhaciyef.prodocument.data.model.UserModel
import com.natiqhaciyef.prodocument.data.network.response.CRUDResponse
import com.natiqhaciyef.prodocument.data.network.response.TokenResponse
import com.natiqhaciyef.prodocument.domain.base.BaseRepository
import com.natiqhaciyef.prodocument.domain.model.UIResult
import com.natiqhaciyef.prodocument.domain.model.mapped.MappedUserModel

interface UserRepository : BaseRepository{

    suspend fun getUser(token: String): UIResult<MappedUserModel>?

    suspend fun createAccount(user: UserModel): TokenResponse?

    suspend fun signIn(email: String, password: String): TokenResponse?

    suspend fun getOtp(email: String): CRUDResponse?

    suspend fun getUserFromLocal(): List<UIResult<MappedUserModel>>?

    suspend fun insertToLocal(userEntity: UserEntity)

    suspend fun removeFromLocal(userEntity: UserEntity)

    suspend fun updateFromLocal(userEntity: UserEntity)

}