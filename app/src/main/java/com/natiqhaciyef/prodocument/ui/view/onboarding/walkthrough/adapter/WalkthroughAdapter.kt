package com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.constants.TWO
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough.OnboardFirstFragment
import com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough.OnboardSecondFragment
import com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough.OnboardThirdFragment


class WalkthroughAdapter(activity: AppCompatActivity, private val itemsCount: Int) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = itemsCount

    override fun createFragment(position: Int): Fragment = when (position) {
        ZERO -> { OnboardFirstFragment() }

        ONE -> { OnboardSecondFragment() }

        TWO -> { OnboardThirdFragment() }

        else -> { OnboardFirstFragment() }
    }
}