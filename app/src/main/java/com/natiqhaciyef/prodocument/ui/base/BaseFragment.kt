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
import com.google.android.material.snackbar.Snackbar

open class BaseFragment : Fragment() {

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
        val deepLink = BaseNavigationDeepLink.getDeepLink(title).toUri()
        requireActivity().startActivity(Intent(Intent.ACTION_VIEW, deepLink))
    }

    fun navigateByTitle(title: String) {
        val destinationId = BaseNavigationDeepLink.getNavGraph(title)
        destinationId.let { findNavController().navigate(it) }
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