package com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentOnboardSecondBinding
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.store.AppStorePref
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class OnboardSecondFragment : BaseFragment() {
    private var _binding: FragmentOnboardSecondBinding? = null
    private val binding: FragmentOnboardSecondBinding
        get() = _binding!!

    private val viewModel: OnboardingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnboardSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            skipButton.setOnClickListener { onSkipButtonClickAction() }
            nextButton.setOnClickListener { navigate(R.id.onboardThirdFragment) }
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