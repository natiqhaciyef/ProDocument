package com.natiqhaciyef.prodocument.ui.view.main.choices.scan.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.natiqhaciyef.prodocument.ui.base.BaseFragment

class ScanViewPagerAdapter(
    private val list: List<Fragment>,
    fragment: Fragment
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }
}