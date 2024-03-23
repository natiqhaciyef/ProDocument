package com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.natiqhaciyef.prodocument.databinding.FragmentProScanOnboardingBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.base.BaseNavigationDeepLink.WALKTHROUGH_ROUTE
import com.natiqhaciyef.prodocument.ui.store.AppStorePrefKeys.TOKEN_KEY
import com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough.viewmodel.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProScanOnboardingFragment : BaseFragment<FragmentProScanOnboardingBinding, OnboardingViewModel>(
    FragmentProScanOnboardingBinding::inflate,
    OnboardingViewModel::class
) {
//    private val viewModel: OnboardingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // for initial state
        observerLiveDataAndHandleAction()
        // getTokenLocalStored()
    }

    private fun getTokenLocalStored() {
        lifecycleScope.launch {
            viewModel?.apply {
                val data = dataStore.readString(requireContext(), TOKEN_KEY)
                getUserByToken(data)

                observerLiveDataAndHandleAction()
            }
        }
    }


    private fun observerLiveDataAndHandleAction() {
        viewModel?.apply {
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