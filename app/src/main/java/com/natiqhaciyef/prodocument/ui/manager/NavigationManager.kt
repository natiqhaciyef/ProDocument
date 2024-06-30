package com.natiqhaciyef.prodocument.ui.manager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.files.FileBottomSheetOptionFragment
import com.natiqhaciyef.prodocument.ui.view.main.home.HomeFragment
import com.natiqhaciyef.prodocument.ui.view.main.premium.SubscriptionFragment
import com.natiqhaciyef.prodocument.ui.view.onboarding.OnboardingActivity
import com.natiqhaciyef.prodocument.ui.view.registration.RegistrationActivity


object NavigationManager {
    const val RESOURCE_BUNDLE = "resourceBundle"
    const val DATASET_BUNDLE = "datasetBundle"

    // Deep links
    const val REGISTER_MAIN_DEEPLINK = "prodoc://register/main"
    const val ONBOARDING_MAIN_DEEPLINK = "prodoc://onboarding/main"
    const val HOME_MAIN_DEEPLINK = "prodoc://home/main"

    const val PROTECT_TYPE = "ProtectPdfType"
    const val SPLIT_TYPE = "SplitPdfType"
    const val COMPRESS_TYPE = "CompressPdfType"
    const val E_SIGN_TYPE = "E-SignType"


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
    const val PAYMENT_ROUTE = "Payment"

    // NavParams

    private fun generateNavGraphs(title: String) = when (title) {
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

        SPLIT_ROUTE, PROTECT_ROUTE, COMPRESS_ROUTE, E_SIGN_ROUTE -> {
            R.navigation.pick_file_nav_graph
        }

        MERGE_ROUTE -> {
            R.navigation.merge_nav_graph
        }

        ALL_TOOLS_ROUTE -> {
            ZERO
        }

        // Result Route
        SUCCESS_ROUTE -> {
            ZERO
        }

        ERROR_ROUTE -> {
            ZERO
        }

        CUSTOM_ROUTE -> {
            ZERO
        }

        // External Routes
        MODIFY_PDF_ROUTE -> {
            R.navigation.preview_material_nav_graph
        }

        // External Routes
        PAYMENT_ROUTE -> {
            R.navigation.payment_nav_graph
        }

        else -> ZERO
    }

    private fun getNavigationByTitleActivity(title: String, context: Context) = when (title) {
        ONBOARDING_ROUTE -> {
            Intent(context, OnboardingActivity::class.java)
        }

        REGISTER_ROUTE -> {
            Intent(context, RegistrationActivity::class.java)
        }

        HOME_ROUTE -> {
            Intent(context, MainActivity::class.java)
        }

        else -> Intent(context, RegistrationActivity::class.java)
    }


    fun navigateByActivityTitle(title: String, activity: Activity, isFinished: Boolean = false) {
        val destinationIntent = getNavigationByTitleActivity(title, activity)
        destinationIntent.let {
            activity.startActivity(destinationIntent)
            activity.overridePendingTransition(
                com.natiqhaciyef.common.R.anim.slide_in_right,
                com.natiqhaciyef.common.R.anim.slide_out_left
            )
            if (isFinished)
                activity.finish()
        }
    }

    private fun getDeepLink(title: String) = when (title) {
        ONBOARDING_ROUTE -> {
            Intent(Intent.ACTION_VIEW, ONBOARDING_MAIN_DEEPLINK.toUri())
        }

        REGISTER_ROUTE -> {
            Intent(Intent.ACTION_VIEW, REGISTER_MAIN_DEEPLINK.toUri())
        }

        HOME_ROUTE -> {
            Intent(Intent.ACTION_VIEW, HOME_MAIN_DEEPLINK.toUri())
        }

        else -> Intent(Intent.ACTION_VIEW, REGISTER_MAIN_DEEPLINK.toUri())
    }

    private fun getNavGraph(title: String) = generateNavGraphs(title)


    fun navigateByDeepLink(activity: Activity, title: String) {
        val deepLink = getDeepLink(title)
        activity.startActivity(deepLink)
    }

    fun navigateByRouteTitle(fragment: Fragment, title: String, bundle: Bundle? = null) {
        when (title) {
            COMPRESS_ROUTE, SPLIT_ROUTE, PROTECT_ROUTE, E_SIGN_ROUTE -> {
                if ((fragment is HomeFragment || (fragment is FileBottomSheetOptionFragment)) && bundle != null) {
                    val customBundle = bundleOf()
                    customBundle.putBundle(RESOURCE_BUNDLE, bundle)

                    val destinationId = getNavGraph(title)
                    fragment.findNavController().setGraph(destinationId, customBundle)
                }
            }

            PAYMENT_ROUTE -> {
                if (fragment is SubscriptionFragment && bundle != null) {
                    val customBundle = bundleOf()
                    customBundle.putBundle(DATASET_BUNDLE, bundle)

                    val destinationId = getNavGraph(title)
                    fragment.findNavController().setGraph(destinationId, customBundle)
                }
            }

            else -> {
                val destinationId = getNavGraph(title)
                destinationId.let { fragment.findNavController().setGraph(destinationId) }
            }
        }
    }

}