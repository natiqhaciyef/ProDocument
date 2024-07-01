package com.natiqhaciyef.common.model

import android.content.Context

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
    }
}