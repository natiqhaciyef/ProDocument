package com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.natiqhaciyef.prodocument.databinding.FragmentOnboardThirdBinding
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.ui.util.BaseNavigationDeepLink.navigateByActivityTitle
import com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough.contract.OnBoardingContract
import com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough.viewmodel.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

@AndroidEntryPoint
class OnboardThirdFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOnboardThirdBinding =
        FragmentOnboardThirdBinding::inflate,
    override val viewModelClass: KClass<OnboardingViewModel> = OnboardingViewModel::class
) : BaseFragment<FragmentOnboardThirdBinding, OnboardingViewModel, OnBoardingContract.OnboardingState, OnBoardingContract.OnBoardingEvent, OnBoardingContract.OnboardingEffect>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            skipButton.setOnClickListener { onSkipButtonClickEvent() }
            nextButton.setOnClickListener { onSkipButtonClickEvent() }
        }
    }

    private fun onSkipButtonClickEvent() {
        lifecycleScope.launch {
            dataStore.saveBoolean(context = requireContext(), enabled = true)

            viewModel.postEvent(OnBoardingContract.OnBoardingEvent.SkipButtonClickEvent{ route ->
                navigateByActivityTitle(route, requireActivity(), true)
            })
        }
    }
}