package com.natiqhaciyef.prodocument.ui.view.main.profile.params.personalinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.natiqhaciyef.common.helpers.loadImage
import com.natiqhaciyef.common.model.mapped.MappedUserWithoutPasswordModel
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentPersonalInfoBinding
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
        viewModel.postEvent(ProfileContract.ProfileEvent.GetUser)
        activityConfig()
    }

    override fun onStateChange(state: ProfileContract.ProfileState) {
        when {
            state.isLoading -> {

            }

            else -> {

                if (state.user != null)
                    defaultParams(state.user!!)
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
                    setTitleToolbar(getString(com.natiqhaciyef.common.R.string.personal_info))
                    changeVisibility(View.VISIBLE)
                    changeAppIcon(com.natiqhaciyef.common.R.drawable.back_arrow_icon){ onToolbarBackPressAction(bottomNavBar) }
                    appIconVisibility(View.VISIBLE)
                    setVisibilitySearch(View.GONE)
                    setVisibilityOptionsMenu(View.VISIBLE)
                    setIconToOptions(com.natiqhaciyef.common.R.drawable.options_icon)
                    setVisibilityToolbar(View.VISIBLE)
                }
            }
        }
    }

    private fun defaultParams(user: MappedUserWithoutPasswordModel) {
        with(binding) {
            userImage.loadImage(user.imageUrl)
            fullNameInput.insertInput(user.name)
            emailInput.insertInput(user.email)
            phoneNumberInput.insertInput(user.phoneNumber)
            genderInput.initPickedData(user.gender)
            dateOfBirthInput.insertInput(user.birthDate)

            if (user.country.isNotEmpty())
                countryInput.insertInput(user.country)

            if (user.city.isNotEmpty())
                cityInput.insertInput(user.city)

            if (user.street.isNotEmpty())
                streetAddressInput.insertInput(user.street)
        }
    }

    private fun onToolbarBackPressAction(bnw: BottomNavigationView) {
        bnw.visibility = View.VISIBLE
        navigate(R.id.profileFragment)
    }
}