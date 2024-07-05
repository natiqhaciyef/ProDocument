package com.natiqhaciyef.prodocument.ui.view.main.profile.params.helpcenter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.natiqhaciyef.common.model.ContactMethods
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.core.base.ui.BaseRecyclerHolderStatefulFragment
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
) : BaseRecyclerHolderStatefulFragment<
        FragmentContactBinding, ProfileViewModel, ContactMethods, ContactAdapter,
        ProfileContract.ProfileState, ProfileContract.ProfileEvent, ProfileContract.ProfileEffect>() {
    override var adapter: ContactAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.postEvent(ProfileContract.ProfileEvent.GetContactMethods)
    }

    override fun onStateChange(state: ProfileContract.ProfileState) {
        when {
            state.isLoading -> changeVisibilityOfProgressBar(true)

            else -> {
                changeVisibilityOfProgressBar()
                if (state.contactMethods != null)
                    recyclerViewConfig(state.contactMethods!!)
            }
        }
    }

    override fun onEffectUpdate(effect: ProfileContract.ProfileEffect) {

    }

    private fun changeVisibilityOfProgressBar(isVisible: Boolean = false) {
        if (isVisible) {
            binding.apply {
                progressBar.visibility = View.VISIBLE
                progressBar.isIndeterminate = true
            }
        } else {
            binding.apply {
                progressBar.visibility = View.GONE
                progressBar.isIndeterminate = false
            }
        }
    }


    override fun recyclerViewConfig(list: List<ContactMethods>) {
        with(binding) {
            adapter = ContactAdapter(list.toMutableList())
            recyclerContactView.adapter = adapter
            recyclerContactView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            adapter?.onClickAction = { method ->
                // add share or contact architecture
            }
        }
    }
}