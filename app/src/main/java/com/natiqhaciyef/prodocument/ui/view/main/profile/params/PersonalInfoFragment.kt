package com.natiqhaciyef.prodocument.ui.view.main.profile.params

import android.os.Bundle
import android.provider.ContactsContract.Profile
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentPersonalInfoBinding
import com.natiqhaciyef.prodocument.ui.util.NavigationManager
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.profile.contract.ProfileContract
import com.natiqhaciyef.prodocument.ui.view.main.profile.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass


@AndroidEntryPoint
class PersonalInfoFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPersonalInfoBinding = FragmentPersonalInfoBinding::inflate,
    override val viewModelClass: KClass<ProfileViewModel> = ProfileViewModel::class
) : BaseFragment<FragmentPersonalInfoBinding, ProfileViewModel, ProfileContract.ProfileState, ProfileContract.ProfileEvent, ProfileContract.ProfileEffect>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityConfig()
    }

    override fun onStateChange(state: ProfileContract.ProfileState) {
        when{
            state.isLoading -> {

            }

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
                appbarLayout.visibility = View.VISIBLE

                materialToolbar.apply {
                    visibility = View.VISIBLE
                    setTitleToolbar(getString(com.natiqhaciyef.common.R.string.personal_info))
                    changeVisibility(View.VISIBLE)
                    appIconVisibility(View.GONE)
                    setVisibilitySearch(View.GONE)
                    setVisibilityOptionsMenu(View.GONE)
                    setIconToOptions(com.natiqhaciyef.common.R.drawable.toolbar_scan_icon)
                    setVisibilityToolbar(View.VISIBLE)
                    setNavigationOnClickListener { onToolbarBackPressAction(bottomNavBar) }
                }
            }
        }
    }

    private fun onToolbarBackPressAction(bnw: BottomNavigationView){
        bnw.visibility = View.VISIBLE
        navigate(R.id.profileFragment)
    }
}