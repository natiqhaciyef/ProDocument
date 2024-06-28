package com.natiqhaciyef.data.network.manager

import android.content.Context
import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.common.constants.MATERIAL_TOKEN_MOCK_KEY
import com.natiqhaciyef.core.store.AppStorePref
import com.natiqhaciyef.core.store.AppStorePrefKeys
import com.natiqhaciyef.data.network.NetworkConfig

class TokenManager(
    private val ctx: Context
) {
    private val ds = AppStorePref

    suspend fun saveTokens(token: MappedTokenModel) {
        ds.saveParcelableClassData(ctx, token, AppStorePrefKeys.TOKEN_KEY)
    }

    suspend fun getTokens(): MappedTokenModel? {
        return ds.readParcelableClassData(
            ctx,
            MappedTokenModel::class.java,
            AppStorePrefKeys.TOKEN_KEY
        )
    }

    suspend fun removeToken(){
        return ds.removeParcelableClassData(ctx, AppStorePrefKeys.TOKEN_KEY)
    }

    suspend fun generateToken(): String{
        val token = AppStorePref.readParcelableClassData(
            ctx,
            MappedTokenModel::class.java,
            AppStorePrefKeys.TOKEN_KEY
        )?.accessToken ?: MATERIAL_TOKEN_MOCK_KEY

        return NetworkConfig.HEADER_AUTHORIZATION_TYPE + token
    }
}