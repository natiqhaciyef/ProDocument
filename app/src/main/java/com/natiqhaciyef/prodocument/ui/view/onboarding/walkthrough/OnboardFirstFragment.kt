package com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentOnboardFirstBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough.event.OnBoardingEvent
import com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough.viewmodel.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

@AndroidEntryPoint
class OnboardFirstFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOnboardFirstBinding = FragmentOnboardFirstBinding::inflate,
    override val viewModelClass: KClass<OnboardingViewModel> = OnboardingViewModel::class
) : BaseFragment<FragmentOnboardFirstBinding, OnboardingViewModel, OnBoardingEvent>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            skipButton.setOnClickListener { onSkipButtonClickAction() }
            nextButton.setOnClickListener { navigate(R.id.onboardSecondFragment) }
        }
    }

    private fun onSkipButtonClickAction() {
        lifecycleScope.launch {
            dataStore.saveBoolean(context = requireContext(), enabled = true)
            viewModel.actionForOnBoarding { route ->
                navigateByActivityTitle(route, true)
            }
        }
    }
}