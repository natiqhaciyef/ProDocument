package com.natiqhaciyef.prodocument.ui.view.onboarding.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough.OnboardFirstFragment
import com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough.OnboardSecondFragment
import com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough.OnboardThirdFragment


class WalkthroughAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> {
            OnboardFirstFragment()
        }

        1 -> {
            OnboardSecondFragment()
        }

        2 -> {
            OnboardThirdFragment()
        }

        else -> {
            OnboardFirstFragment()
        }
    }

}