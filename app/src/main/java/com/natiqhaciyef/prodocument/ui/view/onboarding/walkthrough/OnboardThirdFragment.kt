package com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.natiqhaciyef.prodocument.databinding.FragmentOnboardThirdBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough.viewmodel.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnboardThirdFragment : BaseFragment<FragmentOnboardThirdBinding, OnboardingViewModel>(
    FragmentOnboardThirdBinding::inflate,
    OnboardingViewModel::class
) {
//    private val viewModel: OnboardingViewModel by viewModels()

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
            viewModel?.actionForOnBoarding { route ->
                navigateByActivityTitle(route, true)
            }
        }
    }
}