package com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.natiqhaciyef.common.helpers.toMappedToken
import com.natiqhaciyef.prodocument.databinding.FragmentProScanOnboardingBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.base.BaseNavigationDeepLink.WALKTHROUGH_ROUTE
import com.natiqhaciyef.prodocument.ui.store.AppStorePrefKeys.TOKEN_KEY
import com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough.event.OnBoardingEvent
import com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough.viewmodel.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

@AndroidEntryPoint
class ProScanOnboardingFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProScanOnboardingBinding = FragmentProScanOnboardingBinding::inflate,
    override val viewModelClass: KClass<OnboardingViewModel> = OnboardingViewModel::class
) : BaseFragment<FragmentProScanOnboardingBinding, OnboardingViewModel, OnBoardingEvent>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // for initial state
        observerLiveDataAndHandleAction()
        // getTokenLocalStored()
    }

    private fun getTokenLocalStored() {
        lifecycleScope.launch {
            viewModel.apply {
                val data = dataStore.readString(requireContext(), TOKEN_KEY).toMappedToken()
                getUserByToken(data)

                observerLiveDataAndHandleAction()
            }
        }
    }


    private fun observerLiveDataAndHandleAction() {
        viewModel.apply {
            userState.observe(viewLifecycleOwner) { userState ->
                onboardingAction { route ->
                    lifecycleScope.launch {
                        if (dataStore.readBoolean(requireContext()))
                            navigateByActivityTitle(route, true)
                        else
                            navigateByRouteTitle(WALKTHROUGH_ROUTE)
                    }
                }
            }
        }
    }
}