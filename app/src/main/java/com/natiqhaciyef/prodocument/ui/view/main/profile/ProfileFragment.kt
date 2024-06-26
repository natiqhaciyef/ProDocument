package com.natiqhaciyef.prodocument.ui.view.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.natiqhaciyef.common.model.LanguageModel
import com.natiqhaciyef.common.model.mapped.MappedSubscriptionModel
import com.natiqhaciyef.common.model.mapped.MappedUserWithoutPasswordModel
import com.natiqhaciyef.prodocument.ui.view.main.profile.model.AccountSettingModel
import com.natiqhaciyef.prodocument.databinding.FragmentProfileBinding
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.prodocument.ui.manager.LanguageManager
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.profile.adapter.AccountParametersAdapter
import com.natiqhaciyef.prodocument.ui.view.main.profile.contract.ProfileContract
import com.natiqhaciyef.prodocument.ui.view.main.profile.params.LogOutFragment
import com.natiqhaciyef.prodocument.ui.view.main.profile.params.language.LanguageFragment
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
        viewModel.postEvent(ProfileContract.ProfileEvent.GetAccountInfo)
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

                if (state.pickedPlan != null && state.user != null)
                    initAccountInfo(state.user!!, state.pickedPlan!!)

                if (state.user != null && state.pickedPlan == null)
                    viewModel.postEvent(ProfileContract.ProfileEvent.GetSubscriptionInfo(state.user!!))

                if (state.languages != null)
                    initLanguages(state.languages!!.toMutableList())
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
                setTitleToolbar(getString(R.string.proscan))
                changeAppIcon(R.drawable.pro_scan_lens_icon)
                changeVisibility(View.VISIBLE)
                setVisibilitySearch(View.GONE)
                setVisibilityOptionsMenu(View.GONE)
                setIconToOptions(R.drawable.toolbar_scan_icon)
                setVisibilityToolbar(View.VISIBLE)
            }
        }
    }

    private fun recyclerviewConfig(list: MutableList<AccountSettingModel>) {
        adapter = AccountParametersAdapter(this, list)
        adapter?.onClickAction = { title -> adapterClickNavigation(title) }
        with(binding) {
            recyclerSettingsView.adapter = adapter
            recyclerSettingsView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun initAccountInfo(
        user: MappedUserWithoutPasswordModel,
        plan: MappedSubscriptionModel
    ) {
        with(binding) {
            profileDetailsTopbar.initAccountDetails(
                user = user,
                subscriptionType = plan.title,
                filled = ZERO.toDouble(),
                total = plan.size,
                type = plan.sizeType
            )
        }
    }

    private fun initLanguages(list: MutableList<LanguageModel>) {
        LanguageFragment.list = list
        LanguageFragment { language ->
            // change language
            LanguageManager.setLocaleLang(lang = language.title, requireContext())
            LanguageManager.loadLocale(requireContext())
        }.show(
            if (!isAdded) return else this.childFragmentManager,
            LanguageFragment::class.simpleName
        )
    }

    private fun adapterClickNavigation(title: String) {
        when (title.lowercase()) {
            requireContext().getString(R.string.language).lowercase() -> {
                viewModel.postEvent(ProfileContract.ProfileEvent.GetAllSupportedLanguages(requireContext()))
            }

            requireContext().getString(R.string.logout).lowercase() -> {
                LogOutFragment(
                    onYesClick = {},
                    onCancelClick = {}
                ).show(
                    if (!isAdded) return else this.childFragmentManager,
                    LogOutFragment::class.simpleName
                )
            }

            else -> {
                val action =
                    ProfileFragmentDirections.actionProfileFragmentToProfileDetailsNavGraph(title)
                navigate(action)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.postEvent(ProfileContract.ProfileEvent.ClearState)
    }
}