package com.natiqhaciyef.prodocument.ui.base

import android.content.Intent
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.onboarding.OnboardingActivity
import com.natiqhaciyef.prodocument.ui.view.registration.RegistrationActivity


object BaseNavigationDeepLink {
    // Deep links
    const val REGISTER_MAIN_DEEPLINK = "prodoc://register/main"
    const val REGISTER_FORGOT_PASSWORD_DEEPLINK = "prodoc://register/forgot_password"
    const val ONBOARDING_MAIN_DEEPLINK = "prodoc://onboarding/main"

    const val HOME_MAIN_DEEPLINK = "prodoc://home/main"

    const val SCAN_MAIN_DEEPLINK = "prodoc://scan/main"
    const val WATERMARK_MAIN_DEEPLINK = "prodoc://watermark/main"
    const val E_SIGN_MAIN_DEEPLINK = "prodoc://e-sign/main"
    const val SPLIT_MAIN_DEEPLINK = "prodoc://split/main"
    const val MERGE_MAIN_DEEPLINK = "prodoc://merge/main"
    const val PROTECT_MAIN_DEEPLINK = "prodoc://protect/main"
    const val COMPRESS_MAIN_DEEPLINK = "prodoc://compress/main"
    const val ALL_TOOLS_MAIN_DEEPLINK = "prodoc://all-tools/main"

    const val SUCCESS_DEEPLINK = "prodoc://success"
    const val ERROR_DEEPLINK = "prodoc://error"
    const val CUSTOM_MAIN_DEEPLINK = "prodoc://custom/main"


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
}