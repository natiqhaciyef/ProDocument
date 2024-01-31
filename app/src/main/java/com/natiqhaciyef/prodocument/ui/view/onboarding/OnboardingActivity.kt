package com.natiqhaciyef.prodocument.ui.view.onboarding

import android.os.Bundle
import com.natiqhaciyef.prodocument.databinding.ActivityOnboardingBinding
import com.natiqhaciyef.prodocument.ui.base.BaseActivity

class OnboardingActivity : BaseActivity() {
    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}