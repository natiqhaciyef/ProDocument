package com.natiqhaciyef.prodocument.ui.view.main.profile.params.helpcenter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentHelpCenterBinding
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.custom.ViewPagerAdapter
import com.natiqhaciyef.prodocument.ui.view.main.profile.contract.ProfileContract
import com.natiqhaciyef.prodocument.ui.view.main.profile.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class HelpCenterFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHelpCenterBinding = FragmentHelpCenterBinding::inflate,
    override val viewModelClass: KClass<ProfileViewModel> = ProfileViewModel::class
) : BaseFragment<FragmentHelpCenterBinding, ProfileViewModel,ProfileContract.ProfileState,ProfileContract.ProfileEvent, ProfileContract.ProfileEffect>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityConfig()
        tabLayoutConfig()
    }

    override fun onStateChange(state: ProfileContract.ProfileState) {
        when{
            state.isLoading -> {}

            else -> {

            }
        }
    }

    override fun onEffectUpdate(effect: ProfileContract.ProfileEffect) {

    }

    private fun activityConfig() {
        (activity as MainActivity).also {
            with(it.binding) {
                bottomNavBar.visibility = View.GONE
                materialToolbar.apply {
                    navigationIcon = null
                    visibility = View.VISIBLE
                    setTitleToolbar(getString(com.natiqhaciyef.common.R.string.help_center))
                    appIconVisibility(View.VISIBLE)
                    setVisibilitySearch(View.GONE)
                    setVisibilityOptionsMenu(View.GONE)
                    changeAppIcon(com.natiqhaciyef.common.R.drawable.back_arrow_icon){
                        bottomNavBar.visibility = View.VISIBLE
                        navigate(R.id.profileFragment)
                    }
                }
            }
        }
    }

    private fun tabLayoutConfig(){
        val fragments = listOf(FAQFragment(), ContactFragment())
        val adapter = ViewPagerAdapter(fragments, this@HelpCenterFragment)
        with(binding){
            helpCenterViewPager.adapter = adapter
            TabLayoutMediator(helpCenterTabLayout, helpCenterViewPager){ tab, position ->
                tab.text = when(position){
                    ZERO -> { getString(com.natiqhaciyef.common.R.string.faq) }
                    ONE -> { getString(com.natiqhaciyef.common.R.string.contact_us) }
                    else -> { getString(com.natiqhaciyef.common.R.string.faq)}
                }
            }.attach()

            helpCenterViewPager.isUserInputEnabled = false
        }
    }

}