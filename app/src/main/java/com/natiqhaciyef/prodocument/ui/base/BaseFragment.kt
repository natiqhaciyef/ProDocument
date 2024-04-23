package com.natiqhaciyef.prodocument.ui.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.ui.base.BaseNavigationDeepLink.HOME_MAIN_DEEPLINK
import com.natiqhaciyef.prodocument.ui.base.BaseNavigationDeepLink.ONBOARDING_MAIN_DEEPLINK
import com.natiqhaciyef.prodocument.ui.base.BaseNavigationDeepLink.REGISTER_MAIN_DEEPLINK
import com.natiqhaciyef.prodocument.ui.store.AppStorePref
import com.natiqhaciyef.prodocument.ui.store.AppStorePrefKeys
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.onboarding.OnboardingActivity
import com.natiqhaciyef.prodocument.ui.view.registration.RegistrationActivity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel<State, Event, Effect>, State : UiState, Event : UiEvent, Effect : UiEffect> :
    Fragment() {
    abstract val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
    abstract val viewModelClass: KClass<VM>

    protected var _binding: VB? = null
    val binding: VB
        get() = _binding!!

    val viewModel: VM
        get() {
            viewModelClass.let { return ViewModelProvider(this)[viewModelClass.java] }
        }

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

    protected open fun onStateChange(state: State) {}

    protected open fun onEffectUpdate(effect: Effect) {}

    override fun onDestroy() {
        super.onDestroy()
//        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindInflater(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onStateSubscribers()
    }

    private fun onStateSubscribers() {
        viewModel.state.onEach {
            onStateChange(it)
        }.launchIn(viewModel.viewModelScope)
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
            R.navigation.merge_nav_graph
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
                com.natiqhaciyef.common.R.anim.slide_in_right,
                com.natiqhaciyef.common.R.anim.slide_out_left
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

    protected fun getEmail(onSuccess: (String) -> Unit = { }) = lifecycleScope.launch {
        val result = dataStore.readString(
            context = requireContext(),
            key = AppStorePrefKeys.EMAIL_KEY
        )

        onSuccess(result)
        return@launch
    }
}