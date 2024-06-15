package com.natiqhaciyef.prodocument.ui.view.main.profile.model

import android.content.Context
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.ui.view.main.profile.ProfileFragment

enum class Settings {
    PERSONAL_INFO,
    PREFERENCE,
    SECURITY,
    PAYMENT_HISTORY,
    CATEGORY_GRAPH,
    LANGUAGE,
    DARK_MODE,
    HELP_CENTER,
    ABOUT_PROSCAN,
    LOGOUT;

    companion object{
        fun settingEnumToString(settings: Settings, context: Context) = when(settings){
            PERSONAL_INFO -> { context.getString(com.natiqhaciyef.common.R.string.personal_info) }
            PREFERENCE -> { context.getString(com.natiqhaciyef.common.R.string.preferences) }
            SECURITY -> { context.getString(com.natiqhaciyef.common.R.string.security) }
            PAYMENT_HISTORY -> { context.getString(com.natiqhaciyef.common.R.string.payment_history) }
            CATEGORY_GRAPH -> { context.getString(com.natiqhaciyef.common.R.string.category_graph) }
            LANGUAGE -> { context.getString(com.natiqhaciyef.common.R.string.language) }
            DARK_MODE -> { context.getString(com.natiqhaciyef.common.R.string.dark_mode) }
            HELP_CENTER -> { context.getString(com.natiqhaciyef.common.R.string.help_center) }
            ABOUT_PROSCAN -> { context.getString(com.natiqhaciyef.common.R.string.about_proscan) }
            LOGOUT -> { context.getString(com.natiqhaciyef.common.R.string.logout) }
        }

        fun settingEnumToNavigation(settings: Settings) = when(settings){
            PERSONAL_INFO -> { R.id.personalInfoFragment }
            PREFERENCE -> { R.id.preferencesFragment }
            SECURITY -> { R.id.securityFragment }
            PAYMENT_HISTORY -> { R.id.paymentHistoryFragment }
            CATEGORY_GRAPH -> { R.id.categoryGraphFragment }
            HELP_CENTER -> { R.id.helpCenterFragment }
            ABOUT_PROSCAN -> { R.id.aboutProscanFragment }
            else -> { null }
        }

        fun stringToNavigation(str: String) = when(str){
            PERSONAL_INFO.name -> { R.id.personalInfoFragment }
            PREFERENCE.name -> { R.id.preferencesFragment }
            SECURITY.name -> { R.id.securityFragment }
            PAYMENT_HISTORY.name -> { R.id.paymentHistoryFragment }
            CATEGORY_GRAPH.name -> { R.id.categoryGraphFragment }
            HELP_CENTER.name -> { R.id.helpCenterFragment }
            ABOUT_PROSCAN.name -> { R.id.aboutProscanFragment }
            else -> { null }
        }
    }
}