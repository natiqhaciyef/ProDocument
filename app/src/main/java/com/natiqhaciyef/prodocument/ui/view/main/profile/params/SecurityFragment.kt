package com.natiqhaciyef.prodocument.ui.view.main.profile.params

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentSecurityBinding
import com.natiqhaciyef.prodocument.ui.view.main.profile.contract.ProfileContract
import com.natiqhaciyef.prodocument.ui.view.main.profile.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass


@AndroidEntryPoint
class SecurityFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSecurityBinding = FragmentSecurityBinding::inflate,
    override val viewModelClass: KClass<ProfileViewModel> = ProfileViewModel::class
) : BaseFragment<FragmentSecurityBinding, ProfileViewModel, ProfileContract.ProfileState, ProfileContract.ProfileEvent, ProfileContract.ProfileEffect>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStateChange(state: ProfileContract.ProfileState) {
        when{
            state.isLoading -> {}

            else -> {}
        }
    }

    override fun onEffectUpdate(effect: ProfileContract.ProfileEffect) {

    }
}