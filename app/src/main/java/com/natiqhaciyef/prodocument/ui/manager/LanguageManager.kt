package com.natiqhaciyef.prodocument.ui.manager

import android.app.Activity
import android.content.Context
import java.util.Locale

object LanguageManager {
    fun setLocaleLang(lang: String, context: Context) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)

        val editor = context.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang", lang)
        editor.apply()
    }

    fun loadLocale(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "")
        setLocaleLang(language!!, context)
        return language
    }
}