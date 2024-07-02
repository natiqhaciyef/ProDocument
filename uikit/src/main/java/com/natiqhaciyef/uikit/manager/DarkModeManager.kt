package com.natiqhaciyef.uikit.manager

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

class DarkModeManager {
    private var isNightModeEnabled: Boolean = false

    private constructor()

    constructor(context: Context){
        setConfigCurrentMode(context)
    }

    private fun setConfigCurrentMode(context: Context) {
        isNightModeEnabled =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

    fun getCurrentMode() = isNightModeEnabled

    fun updateCurrentMode() {
        isNightModeEnabled = !isNightModeEnabled
    }

    fun setCurrentMode(current: Boolean) {
        isNightModeEnabled = current
    }

    fun changeModeToggle() {
        if (isNightModeEnabled)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}