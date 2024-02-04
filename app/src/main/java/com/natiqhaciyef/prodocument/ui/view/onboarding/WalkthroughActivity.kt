package com.natiqhaciyef.prodocument.ui.view.onboarding

import android.os.Bundle
import com.natiqhaciyef.prodocument.databinding.ActivityWalkthroughBinding
import com.natiqhaciyef.prodocument.ui.base.BaseActivity
import com.natiqhaciyef.prodocument.ui.view.onboarding.adapter.WalkthroughAdapter
import com.natiqhaciyef.prodocument.ui.view.onboarding.behaviour.ZoomOutPageTransformer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WalkthroughActivity : BaseActivity() {
    private lateinit var binding: ActivityWalkthroughBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalkthroughBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            val adapter = WalkthroughAdapter(this@WalkthroughActivity, 3)
            walkthroughViewPager.adapter = adapter
            walkthroughViewPager.setPageTransformer(ZoomOutPageTransformer())
        }
    }

    override fun onBackPressed() {
        binding.apply {
            if (walkthroughViewPager.currentItem == 0) {
                super.onBackPressed()
            } else {
                walkthroughViewPager.currentItem = walkthroughViewPager.currentItem - 1
            }
        }
    }
}