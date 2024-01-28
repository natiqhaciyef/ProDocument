package com.natiqhaciyef.prodocument.ui.base


object BaseNavigationDeepLink {
    // Deep links
    const val REGISTER_MAIN_DEEPLINK = "prodoc://register/main"
    const val HOME_MAIN_DEEPLINK = "prodoc://home/main"

    const val SCAN_MAIN_DEEPLINK = "prodoc://scan/main"
    const val WATERMARK_MAIN_DEEPLINK = "prodoc://watermark/main"
    const val E_SIGN_MAIN_DEEPLINK = "prodoc://e-sign/main"
    const val SPLIT_MAIN_DEEPLINK = "prodoc://split/main"
    const val MERGE_MAIN_DEEPLINK = "prodoc://merge/main"
    const val PROTECT_MAIN_DEEPLINK = "prodoc://protect/main"
    const val COMPRESS_MAIN_DEEPLINK = "prodoc://compress/main"
    const val ALL_TOOLS_MAIN_DEEPLINK = "prodoc://all-tools/main"

    const val SUCCESS_MAIN_DEEPLINK = "prodoc://success/main"
    const val ERROR_MAIN_DEEPLINK = "prodoc://error/main"
    const val CUSTOM_MAIN_DEEPLINK = "prodoc://custom/main"


    // Routes
    const val HOME_ROUTE = "Home"
    const val REGISTER_ROUTE = "Register"

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


    fun getDeepLink(title: String) = when (title) {
        // Main route
        HOME_ROUTE -> { HOME_MAIN_DEEPLINK }
        REGISTER_ROUTE -> { REGISTER_MAIN_DEEPLINK }

        // Home -> Details route
        SCAN_ROUTE -> { SCAN_MAIN_DEEPLINK }
        WATERMARK_ROUTE -> { WATERMARK_MAIN_DEEPLINK }
        E_SIGN_ROUTE -> { E_SIGN_MAIN_DEEPLINK }
        SPLIT_ROUTE -> { SPLIT_MAIN_DEEPLINK }
        MERGE_ROUTE -> { MERGE_MAIN_DEEPLINK }
        PROTECT_ROUTE -> { PROTECT_MAIN_DEEPLINK }
        COMPRESS_ROUTE -> { COMPRESS_MAIN_DEEPLINK }
        ALL_TOOLS_ROUTE -> { ALL_TOOLS_MAIN_DEEPLINK }

        // Result route
        SUCCESS_ROUTE -> { SUCCESS_MAIN_DEEPLINK }
        ERROR_ROUTE -> { ERROR_MAIN_DEEPLINK }
        else -> { CUSTOM_MAIN_DEEPLINK }
    }

    fun getNavGraph(title: String) = when (title) {
        // Main route
        REGISTER_ROUTE -> { 0 }
        HOME_ROUTE -> { 0 }

        // Home -> Details route
        SCAN_ROUTE -> { 0 }
        WATERMARK_ROUTE -> { 0 }
        E_SIGN_ROUTE -> { 0 }
        SPLIT_ROUTE -> { 0 }
        MERGE_ROUTE -> { 0 }
        PROTECT_ROUTE -> { 0 }
        COMPRESS_ROUTE -> { 0 }
        ALL_TOOLS_ROUTE -> { 0 }

        // Result Route
        SUCCESS_ROUTE -> { 0 }
        ERROR_ROUTE -> { 0 }
        CUSTOM_ROUTE -> { 0 }
        else -> 0
    }
}