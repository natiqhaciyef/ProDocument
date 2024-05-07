package com.natiqhaciyef.data.network.manager

import android.content.Context
import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.core.store.AppStorePref
import com.natiqhaciyef.core.store.AppStorePrefKeys

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
}