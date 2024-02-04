package com.natiqhaciyef.prodocument.ui.base

import android.content.Intent
import android.net.Uri
import android.view.MotionEvent
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.onboarding.OnboardingActivity
import com.natiqhaciyef.prodocument.ui.view.onboarding.WalkthroughActivity
import com.natiqhaciyef.prodocument.ui.view.registration.RegistrationActivity

open class BaseFragment : Fragment() {

    private fun setNavigationWithActivity(title: String) = when (title) {
        BaseNavigationDeepLink.ONBOARDING_ROUTE -> {
            Intent(requireContext(), OnboardingActivity::class.java)
        }

        BaseNavigationDeepLink.WALKTHROUGH_ROUTE -> {
            Intent(requireContext(), WalkthroughActivity::class.java)
        }

        BaseNavigationDeepLink.REGISTER_ROUTE -> {
            Intent(requireContext(), RegistrationActivity::class.java)
        }

        BaseNavigationDeepLink.HOME_ROUTE -> {
            Intent(requireContext(), MainActivity::class.java)
        }

        else -> Intent(requireContext(), RegistrationActivity::class.java)
    }

    private fun getDeepLink(title: String) = when (title) {
        // Main route
        BaseNavigationDeepLink.HOME_ROUTE -> {
            BaseNavigationDeepLink.HOME_MAIN_DEEPLINK
        }

        BaseNavigationDeepLink.REGISTER_ROUTE -> {
            BaseNavigationDeepLink.REGISTER_MAIN_DEEPLINK
        }

        // Home -> Details route
        BaseNavigationDeepLink.SCAN_ROUTE -> {
            BaseNavigationDeepLink.SCAN_MAIN_DEEPLINK
        }

        BaseNavigationDeepLink.WATERMARK_ROUTE -> {
            BaseNavigationDeepLink.WATERMARK_MAIN_DEEPLINK
        }

        BaseNavigationDeepLink.E_SIGN_ROUTE -> {
            BaseNavigationDeepLink.E_SIGN_MAIN_DEEPLINK
        }

        BaseNavigationDeepLink.SPLIT_ROUTE -> {
            BaseNavigationDeepLink.SPLIT_MAIN_DEEPLINK
        }

        BaseNavigationDeepLink.MERGE_ROUTE -> {
            BaseNavigationDeepLink.MERGE_MAIN_DEEPLINK
        }

        BaseNavigationDeepLink.PROTECT_ROUTE -> {
            BaseNavigationDeepLink.PROTECT_MAIN_DEEPLINK
        }

        BaseNavigationDeepLink.COMPRESS_ROUTE -> {
            BaseNavigationDeepLink.COMPRESS_MAIN_DEEPLINK
        }

        BaseNavigationDeepLink.ALL_TOOLS_ROUTE -> {
            BaseNavigationDeepLink.ALL_TOOLS_MAIN_DEEPLINK
        }

        // Result route
        BaseNavigationDeepLink.SUCCESS_ROUTE -> {
            BaseNavigationDeepLink.SUCCESS_DEEPLINK
        }

        BaseNavigationDeepLink.ERROR_ROUTE -> {
            BaseNavigationDeepLink.ERROR_DEEPLINK
        }

        else -> {
            BaseNavigationDeepLink.CUSTOM_MAIN_DEEPLINK
        }
    }

    private fun getNavGraph(title: String) = when (title) {
        // Main route
        BaseNavigationDeepLink.REGISTER_ROUTE -> {
            R.navigation.registration_nav_graph
        }

        BaseNavigationDeepLink.HOME_ROUTE -> {
            R.navigation.home_nav_graph
        }

        // Registration route
        BaseNavigationDeepLink.ONBOARDING_ROUTE -> {
            R.navigation.onboarding_nav_graph
        }

        BaseNavigationDeepLink.FORGOT_PASSWORD_ROUTE -> {
            R.navigation.forgot_password_nav_graph
        }

        // Home -> Details route
        BaseNavigationDeepLink.SCAN_ROUTE -> {
            0
        }

        BaseNavigationDeepLink.WATERMARK_ROUTE -> {
            0
        }

        BaseNavigationDeepLink.E_SIGN_ROUTE -> {
            0
        }

        BaseNavigationDeepLink.SPLIT_ROUTE -> {
            0
        }

        BaseNavigationDeepLink.MERGE_ROUTE -> {
            0
        }

        BaseNavigationDeepLink.PROTECT_ROUTE -> {
            0
        }

        BaseNavigationDeepLink.COMPRESS_ROUTE -> {
            0
        }

        BaseNavigationDeepLink.ALL_TOOLS_ROUTE -> {
            0
        }

        // Result Route
        BaseNavigationDeepLink.SUCCESS_ROUTE -> {
            0
        }

        BaseNavigationDeepLink.ERROR_ROUTE -> {
            0
        }

        BaseNavigationDeepLink.CUSTOM_ROUTE -> {
            0
        }

        else -> 0
    }


    fun navigate(id: Int) {
        findNavController().navigate(id)
    }

    fun navigate(direction: NavDirections) {
        findNavController().navigate(direction)
    }

    fun navigateByDeepLink(deepLink: Uri) {
        requireActivity().startActivity(Intent(Intent.ACTION_VIEW, deepLink))
    }

    fun navigateByDeepLink(title: String) {
        val deepLink = getDeepLink(title).toUri()
        requireActivity().startActivity(Intent(Intent.ACTION_VIEW, deepLink))
    }

    fun navigateByRouteTitle(title: String) {
        val destinationId = getNavGraph(title)
        destinationId.let { findNavController().setGraph(destinationId) }
    }

    fun navigateByActivityTitle(title: String, isFinished: Boolean = false) {
        val destinationIntent = setNavigationWithActivity(title)
        destinationIntent.let {
            requireActivity().startActivity(destinationIntent)
            if (isFinished)
                requireActivity().finish()
        }
    }

    fun navigate(
        activity: FragmentActivity,
        intent: Intent,
        isFinished: Boolean = false
    ) {
        activity.startActivity(intent)
        if (isFinished)
            activity.finish()
    }

    fun navigate(deepLink: Uri, navGraph: Int) {
        val pendingIntent = NavDeepLinkBuilder(requireContext())
            .setDestination(deepLink.toString())
            .createPendingIntent()

        pendingIntent.send()
    }

    fun generateSnackbar(title: String) {
        Snackbar.make(requireView(), title, Snackbar.LENGTH_LONG).show()
    }

    fun generateToast(title: String) {
        Toast.makeText(requireContext(), title, Toast.LENGTH_LONG).show()
    }
}