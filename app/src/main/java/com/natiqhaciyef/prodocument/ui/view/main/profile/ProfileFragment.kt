package com.natiqhaciyef.prodocument.ui.view.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.natiqhaciyef.prodocument.ui.view.main.profile.model.AccountSettingModel
import com.natiqhaciyef.prodocument.databinding.FragmentProfileBinding
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.profile.adapter.AccountParametersAdapter
import com.natiqhaciyef.prodocument.ui.view.main.profile.contract.ProfileContract
import com.natiqhaciyef.prodocument.ui.view.main.profile.model.Settings
import com.natiqhaciyef.prodocument.ui.view.main.profile.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass


@AndroidEntryPoint
class ProfileFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProfileBinding = FragmentProfileBinding::inflate,
    override val viewModelClass: KClass<ProfileViewModel> = ProfileViewModel::class
) : BaseFragment<FragmentProfileBinding, ProfileViewModel, ProfileContract.ProfileState, ProfileContract.ProfileEvent, ProfileContract.ProfileEffect>() {
    private var adapter: AccountParametersAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.postEvent(ProfileContract.ProfileEvent.GetSettings)
        activityConfig()
    }

    override fun onStateChange(state: ProfileContract.ProfileState) {
        when {
            state.isLoading -> {
                changeVisibilityOfProgressBar(true)
            }

            else -> {
                changeVisibilityOfProgressBar()

                if (state.settingList != null)
                    recyclerviewConfig(state.settingList!!)
            }
        }
    }

    override fun onEffectUpdate(effect: ProfileContract.ProfileEffect) {

    }

    private fun changeVisibilityOfProgressBar(isVisible: Boolean = false) {
        if (isVisible) {
            binding.apply {
                uiLayout.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                progressBar.isIndeterminate = true
            }
        } else {
            binding.apply {
                uiLayout.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                progressBar.isIndeterminate = false
            }
        }
    }

    private fun activityConfig() {
        (activity as MainActivity).also {
            it.binding.bottomNavBar.visibility = View.VISIBLE
            it.binding.materialToolbar.visibility = View.VISIBLE
            with(it.binding.materialToolbar) {
                visibility = View.VISIBLE
                setTitleToolbar(getString(com.natiqhaciyef.common.R.string.proscan))
                changeAppIcon(com.natiqhaciyef.common.R.drawable.pro_scan_lens_icon)
                changeVisibility(View.VISIBLE)
                setVisibilitySearch(View.GONE)
                setVisibilityOptionsMenu(View.GONE)
                setIconToOptions(com.natiqhaciyef.common.R.drawable.toolbar_scan_icon)
                setVisibilityToolbar(View.VISIBLE)
            }
        }
    }

    private fun recyclerviewConfig(list: MutableList<AccountSettingModel>) {
        adapter = AccountParametersAdapter(this, list)
        with(binding) {
            recyclerSettingsView.adapter = adapter
            recyclerSettingsView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        adapter?.onClickAction = { title ->
            adapterClickNavigation(title)
        }
    }

    private fun adapterClickNavigation(title: String) {
        val action = ProfileFragmentDirections.actionProfileFragmentToProfileDetailsNavGraph(title)
        navigate(action)
    }
}