package com.natiqhaciyef.prodocument.ui.view.main.premium.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class SubscriptionPlanAdapter(
    private val list: List<Fragment>,
    fragment: FragmentActivity
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }
}