package com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.natiqhaciyef.prodocument.databinding.FragmentOnboardThirdBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough.event.OnBoardingEvent
import com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough.viewmodel.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

@AndroidEntryPoint
class OnboardThirdFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOnboardThirdBinding =
        FragmentOnboardThirdBinding::inflate,
    override val viewModelClass: KClass<OnboardingViewModel> = OnboardingViewModel::class
) : BaseFragment<FragmentOnboardThirdBinding, OnboardingViewModel, OnBoardingEvent>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            skipButton.setOnClickListener { onButtonClickAction() }
            nextButton.setOnClickListener { onButtonClickAction() }
        }
    }

    private fun onButtonClickAction() {
        lifecycleScope.launch {
            dataStore.saveBoolean(context = requireContext(), enabled = true)
            viewModel.actionForOnBoarding { route ->
                navigateByActivityTitle(route, true)
            }
        }
    }
}