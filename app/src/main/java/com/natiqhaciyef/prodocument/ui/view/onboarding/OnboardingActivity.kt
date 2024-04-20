package com.natiqhaciyef.prodocument.ui.view.onboarding

import android.os.Bundle
import com.natiqhaciyef.prodocument.databinding.ActivityOnboardingBinding
import com.natiqhaciyef.prodocument.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : BaseActivity<ActivityOnboardingBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}