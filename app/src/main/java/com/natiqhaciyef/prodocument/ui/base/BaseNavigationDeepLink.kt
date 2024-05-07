package com.natiqhaciyef.prodocument.ui.base

import com.natiqhaciyef.prodocument.R


object BaseNavigationDeepLink {
    // Deep links
    const val REGISTER_MAIN_DEEPLINK = "prodoc://register/main"
    const val ONBOARDING_MAIN_DEEPLINK = "prodoc://onboarding/main"
    const val HOME_MAIN_DEEPLINK = "prodoc://home/main"


    // Routes
    const val HOME_ROUTE = "Home"
    const val ONBOARDING_ROUTE = "Onboarding"
    const val WALKTHROUGH_ROUTE = "Walkthrough"
    const val REGISTER_ROUTE = "Register"
    const val FORGOT_PASSWORD_ROUTE = "Forgot Password"

    const val SCAN_ROUTE = "Scan"
    const val WATERMARK_ROUTE = "Watermark"
    const val E_SIGN_ROUTE = "E-Sign"
    const val SPLIT_ROUTE = "Split"
    const val MERGE_ROUTE = "Merge"
    const val PROTECT_ROUTE = "Protect"
    const val COMPRESS_ROUTE = "Compress"
    const val ALL_TOOLS_ROUTE = "All tools"

    const val SUCCESS_ROUTE = "Success"
    const val ERROR_ROUTE = "Error"
    const val CUSTOM_ROUTE = "Custom"

    const val MODIFY_PDF_ROUTE = "Modify PDF"

    // NavParams

    fun generateNavGraphs(title: String) = when (title) {
        // Main route
        REGISTER_ROUTE -> {
            R.navigation.registration_nav_graph
        }

        HOME_ROUTE -> {
            R.navigation.home_nav_graph
        }

        // Registration route
        ONBOARDING_ROUTE -> {
            R.navigation.onboarding_nav_graph
        }

        WALKTHROUGH_ROUTE -> {
            R.navigation.walkthrough_nav_graph
        }

        FORGOT_PASSWORD_ROUTE -> {
            R.navigation.forgot_password_nav_graph
        }

        // Home -> Details route
        SCAN_ROUTE -> {
            R.navigation.scan_nav_graph
        }

        WATERMARK_ROUTE -> {
            R.navigation.watermark_nav_graph
        }

        E_SIGN_ROUTE -> {
            0
        }

        SPLIT_ROUTE -> {
            R.navigation.split_nav_graph
        }

        MERGE_ROUTE -> {
            R.navigation.merge_nav_graph
        }

        PROTECT_ROUTE -> {
            0
        }

        COMPRESS_ROUTE -> {
            0
        }

        ALL_TOOLS_ROUTE -> {
            0
        }

        // Result Route
        SUCCESS_ROUTE -> {
            0
        }

        ERROR_ROUTE -> {
            0
        }

        CUSTOM_ROUTE -> {
            0
        }

        // External Routes
        MODIFY_PDF_ROUTE -> {
            R.navigation.preview_material_nav_graph
        }

        else -> 0
    }
}