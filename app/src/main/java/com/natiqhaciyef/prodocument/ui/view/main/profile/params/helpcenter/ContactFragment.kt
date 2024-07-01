package com.natiqhaciyef.prodocument.ui.view.main.profile.params.helpcenter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.natiqhaciyef.common.model.ContactMethods
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.databinding.FragmentContactBinding
import com.natiqhaciyef.prodocument.ui.view.main.profile.contract.ProfileContract
import com.natiqhaciyef.uikit.adapter.ContactAdapter
import com.natiqhaciyef.prodocument.ui.view.main.profile.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass


@AndroidEntryPoint
class ContactFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentContactBinding = FragmentContactBinding::inflate,
    override val viewModelClass: KClass<ProfileViewModel> = ProfileViewModel::class
) : BaseFragment<FragmentContactBinding, ProfileViewModel, ProfileContract.ProfileState, ProfileContract.ProfileEvent, ProfileContract.ProfileEffect>() {
    private var contactAdapter: ContactAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.postEvent(ProfileContract.ProfileEvent.GetContactMethods)
    }

    override fun onStateChange(state: ProfileContract.ProfileState) {
        when {
            state.isLoading -> {

            }

            else -> {

                if (state.contactMethods != null)
                    contactRecyclerConfig(state.contactMethods!!)
            }
        }
    }

    override fun onEffectUpdate(effect: ProfileContract.ProfileEffect) {

    }


    private fun contactRecyclerConfig(list: List<ContactMethods>) {
        with(binding) {
            contactAdapter = ContactAdapter(list.toMutableList())
            recyclerContactView.adapter = contactAdapter
            recyclerContactView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            contactAdapter?.onClickAction = { method ->
                // add share or contact architecture
            }
        }
    }
}