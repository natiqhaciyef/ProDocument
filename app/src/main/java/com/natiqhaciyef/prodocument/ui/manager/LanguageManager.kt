package com.natiqhaciyef.prodocument.ui.manager

import android.app.Activity
import android.content.Context
import com.natiqhaciyef.common.constants.EMPTY_STRING
import java.util.Locale

object LanguageManager {
    private const val SETTINGS_TAG = "SETTINGS"
    private const val MY_LANGUAGE = "MY_LANG"

    fun setLocaleLang(lang: String, context: Context) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)

        val editor = context.getSharedPreferences(SETTINGS_TAG, Context.MODE_PRIVATE).edit()
        editor.putString(MY_LANGUAGE, lang)
        editor.apply()
    }

    fun loadLocale(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(SETTINGS_TAG, Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString(MY_LANGUAGE, EMPTY_STRING)
        setLocaleLang(language!!, context)
        return language
    }
}