package com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.natiqhaciyef.common.model.mapped.MappedUserWithoutPasswordModel
import com.natiqhaciyef.prodocument.databinding.FragmentProScanOnboardingBinding
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.ui.manager.NavigationManager.WALKTHROUGH_ROUTE
import com.natiqhaciyef.prodocument.ui.manager.NavigationManager.navigateByActivityTitle
import com.natiqhaciyef.prodocument.ui.manager.NavigationManager.navigateByRouteTitle
import com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough.contract.OnBoardingContract
import com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough.viewmodel.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

@AndroidEntryPoint
class ProScanOnboardingFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProScanOnboardingBinding = FragmentProScanOnboardingBinding::inflate,
    override val viewModelClass: KClass<OnboardingViewModel> = OnboardingViewModel::class
) : BaseFragment<FragmentProScanOnboardingBinding, OnboardingViewModel, OnBoardingContract.OnboardingState, OnBoardingContract.OnBoardingEvent, OnBoardingContract.OnboardingEffect>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // for initial state
        onBoardingEvent(null)
//         getTokenLocalStored()
    }

    override fun onStateChange(state: OnBoardingContract.OnboardingState) {
        when{
            state.isLoading -> {}
            else -> {
                onBoardingEvent(user = state.user)
            }
        }
    }

    override fun onEffectUpdate(effect: OnBoardingContract.OnboardingEffect) {

    }

    private fun getTokenLocalStored() {
        lifecycleScope.launch {
            viewModel.postEvent(OnBoardingContract.OnBoardingEvent.GetUserByTokenEvent)
        }
    }

    private fun onBoardingEvent(user: MappedUserWithoutPasswordModel?){
        viewModel.postEvent(OnBoardingContract.OnBoardingEvent.OnboardingEvent(user){ route ->
            lifecycleScope.launch {
                if (dataStore.readBoolean(requireContext()))
                    navigateByActivityTitle(route, requireActivity(),true)
                else
                    navigateByRouteTitle(this@ProScanOnboardingFragment,WALKTHROUGH_ROUTE)
            }
        })
    }
}