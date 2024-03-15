package com.natiqhaciyef.prodocument.ui.base

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.ui.base.BaseNavigationDeepLink.HOME_MAIN_DEEPLINK
import com.natiqhaciyef.prodocument.ui.base.BaseNavigationDeepLink.ONBOARDING_MAIN_DEEPLINK
import com.natiqhaciyef.prodocument.ui.base.BaseNavigationDeepLink.REGISTER_MAIN_DEEPLINK
import com.natiqhaciyef.prodocument.ui.store.AppStorePref
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.onboarding.OnboardingActivity
import com.natiqhaciyef.prodocument.ui.view.registration.RegistrationActivity

open class BaseFragment<VB : ViewBinding> : Fragment() {
    protected var _binding: VB? = null
    val binding: VB
        get() = _binding!!

    val dataStore = AppStorePref

    private fun setNavigationWithActivity(title: String) = when (title) {
        BaseNavigationDeepLink.ONBOARDING_ROUTE -> {
            Intent(requireContext(), OnboardingActivity::class.java)
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
        BaseNavigationDeepLink.ONBOARDING_ROUTE -> {
            Intent(Intent.ACTION_VIEW, ONBOARDING_MAIN_DEEPLINK.toUri())
        }

        BaseNavigationDeepLink.REGISTER_ROUTE -> {
            Intent(Intent.ACTION_VIEW, REGISTER_MAIN_DEEPLINK.toUri())
        }

        BaseNavigationDeepLink.HOME_ROUTE -> {
            Intent(Intent.ACTION_VIEW, HOME_MAIN_DEEPLINK.toUri())
        }

        else -> Intent(Intent.ACTION_VIEW, REGISTER_MAIN_DEEPLINK.toUri())
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

        BaseNavigationDeepLink.WALKTHROUGH_ROUTE -> {
            R.navigation.walkthrough_nav_graph
        }

        BaseNavigationDeepLink.FORGOT_PASSWORD_ROUTE -> {
            R.navigation.forgot_password_nav_graph
        }

        // Home -> Details route
        BaseNavigationDeepLink.SCAN_ROUTE -> {
            R.navigation.scan_nav_graph
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

    fun navigateBack() {
        findNavController().popBackStack()
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
        val deepLink = getDeepLink(title)
        requireActivity().startActivity(deepLink)
    }

    fun navigateByRouteTitle(title: String) {
        val destinationId = getNavGraph(title)
        destinationId.let { findNavController().setGraph(destinationId) }
    }

    fun navigateByActivityTitle(title: String, isFinished: Boolean = false) {
        val destinationIntent = setNavigationWithActivity(title)
        destinationIntent.let {
            requireActivity().startActivity(destinationIntent)
            requireActivity().overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}