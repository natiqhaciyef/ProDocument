package com.natiqhaciyef.prodocument.ui.manager

import androidx.appcompat.app.AppCompatDelegate

object DarkModeManager {
    private var isNightModeEnabled = AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_NO

    fun getCurrentMode() = isNightModeEnabled

    fun updateCurrentMode() {
        isNightModeEnabled = !isNightModeEnabled
    }

    fun setCurrentMode(current: Boolean){
        isNightModeEnabled = current
    }

    fun changeModeToggle(){
        if (isNightModeEnabled)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
}