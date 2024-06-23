package com.natiqhaciyef.prodocument.ui.view.main.profile.params.helpcenter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.databinding.FragmentContactBinding
import com.natiqhaciyef.prodocument.ui.view.main.profile.contract.ProfileContract
import com.natiqhaciyef.prodocument.ui.view.main.profile.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass


@AndroidEntryPoint
class ContactFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentContactBinding = FragmentContactBinding::inflate,
    override val viewModelClass: KClass<ProfileViewModel> = ProfileViewModel::class
) : BaseFragment<FragmentContactBinding, ProfileViewModel, ProfileContract.ProfileState, ProfileContract.ProfileEvent, ProfileContract.ProfileEffect>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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


}