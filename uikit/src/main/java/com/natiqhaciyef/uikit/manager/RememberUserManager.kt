package com.natiqhaciyef.uikit.manager

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.natiqhaciyef.core.store.AppStorePref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object RememberUserManager {
    private val storage = AppStorePref
    private val REMEMBER_ME_KEY = booleanPreferencesKey("REMEMBER_ME")

    private suspend fun getRememberedState(ctx: Context) : Boolean =
        storage.readBoolean(ctx, REMEMBER_ME_KEY)

    fun rememberState(ctx: Context, state: Boolean) {
        CoroutineScope(Dispatchers.Main).launch{
            storage.saveBoolean(context = ctx, enabled = state, key = REMEMBER_ME_KEY)
        }
    }

    suspend fun isRemembered(ctx: Context) = getRememberedState(ctx)
}

