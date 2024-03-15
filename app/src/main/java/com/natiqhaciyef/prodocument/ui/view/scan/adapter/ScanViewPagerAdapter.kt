package com.natiqhaciyef.prodocument.ui.view.scan.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.natiqhaciyef.prodocument.ui.base.BaseFragment

class ScanViewPagerAdapter(
    private val list: List<BaseFragment>,
    fragment: BaseFragment
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }
}