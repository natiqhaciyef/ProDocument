package com.natiqhaciyef.prodocument.ui.view.onboarding

import android.os.Bundle
import com.natiqhaciyef.prodocument.databinding.ActivityWalkthroughBinding
import com.natiqhaciyef.prodocument.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WalkthroughActivity : BaseActivity() {
    private lateinit var binding: ActivityWalkthroughBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalkthroughBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}