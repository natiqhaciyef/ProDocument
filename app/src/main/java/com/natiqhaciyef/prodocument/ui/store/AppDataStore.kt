package com.natiqhaciyef.prodocument.ui.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.natiqhaciyef.prodocument.ui.store.AppStorePrefKeys.BOOLEAN_KEY
import com.natiqhaciyef.prodocument.ui.store.AppStorePrefKeys.INT_KEY
import com.natiqhaciyef.prodocument.ui.store.AppStorePrefKeys.STR_KEY
import com.natiqhaciyef.prodocument.ui.store.AppStorePrefKeys.PARCELABLE_KEY
import kotlinx.coroutines.flow.first


/**
 * AppStorePref created for storing token, walkthrough screen visibility and other custom
 * dataset. Simple and easy to use. By default this class initialized in BaseFragment
 *
 * @author Natig Hajiyev && ProScan Application
 * */

object AppStorePref {
    private val Context.ds: DataStore<Preferences> by preferencesDataStore("data_store")

    suspend fun saveParcelableClassData(
        context: Context,
        data: String,
        key: Preferences.Key<String> = PARCELABLE_KEY
    ) {
        context.ds.edit {
            it[key] = data
        }
    }

    suspend fun saveString(
        context: Context,
        data: String,
        key: Preferences.Key<String> = STR_KEY
    ) {
        context.ds.edit {
            it[key] = data
        }
    }

    suspend fun saveInt(
        context: Context,
        num: Int,
        key: Preferences.Key<Int> = INT_KEY
    ) {
        context.ds.edit {
            it[key] = num
        }
    }

    suspend fun saveBoolean(
        context: Context,
        enabled: Boolean,
        key: Preferences.Key<Boolean> = BOOLEAN_KEY
    ) {
        context.ds.edit {
            it[key] = enabled
        }
    }


    suspend fun readParcelableClassData(
        context: Context,
        key: Preferences.Key<String> = PARCELABLE_KEY
    ): String = context.ds.data.first()[key] ?: "Parcelable class not found"

    suspend fun readString(
        context: Context,
        key: Preferences.Key<String> = STR_KEY
    ): String = context.ds.data.first()[key] ?: "String not found"

    suspend fun readInt(
        context: Context,
        key: Preferences.Key<Int> = INT_KEY
    ): Int = context.ds.data.first()[key] ?: 0

    suspend fun readBoolean(
        context: Context,
        key: Preferences.Key<Boolean> = BOOLEAN_KEY
    ): Boolean = context.ds.data.first()[key] ?: false


    suspend fun removeString(
        context: Context,
        key: Preferences.Key<String> = STR_KEY
    ) {
        context.ds.edit {
            it.remove(key)
        }
    }

    suspend fun removeParcelableClassData(
        context: Context,
        key: Preferences.Key<String> = PARCELABLE_KEY
    ) {
        context.ds.edit {
            it.remove(key)
        }
    }

    suspend fun removeInt(
        context: Context,
        key: Preferences.Key<Int> = INT_KEY
    ) {
        context.ds.edit {
            it.remove(key)
        }
    }

    suspend fun removeBoolean(
        context: Context,
        key: Preferences.Key<Boolean> = BOOLEAN_KEY
    ) {
        context.ds.edit {
            it.remove(key)
        }
    }
}

object AppStorePrefKeys {
    val STR_KEY = stringPreferencesKey("STRING")
    val PARCELABLE_KEY = stringPreferencesKey("PARCELABLE")
    val INT_KEY = intPreferencesKey("INTEGER")
    val BOOLEAN_KEY = booleanPreferencesKey("BOOLEAN")

    val TITLE_COUNT_KEY = intPreferencesKey("TITLE_COUNT_KEY")
    val TOKEN_KEY = stringPreferencesKey("TOKEN_STORED")
}