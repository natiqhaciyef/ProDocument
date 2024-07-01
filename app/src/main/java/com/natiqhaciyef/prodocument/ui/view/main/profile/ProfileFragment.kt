package com.natiqhaciyef.prodocument.ui.view.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.natiqhaciyef.common.model.LanguageModel
import com.natiqhaciyef.common.model.mapped.MappedSubscriptionModel
import com.natiqhaciyef.common.model.mapped.MappedUserWithoutPasswordModel
import com.natiqhaciyef.common.model.AccountSettingModel
import com.natiqhaciyef.prodocument.databinding.FragmentProfileBinding
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.common.model.Settings
import com.natiqhaciyef.uikit.manager.DarkModeManager
import com.natiqhaciyef.uikit.manager.LanguageManager
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.uikit.adapter.AccountParametersAdapter
import com.natiqhaciyef.prodocument.ui.view.main.profile.contract.ProfileContract
import com.natiqhaciyef.prodocument.ui.view.main.profile.params.LogOutFragment
import com.natiqhaciyef.prodocument.ui.view.main.profile.params.language.LanguageFragment
import com.natiqhaciyef.prodocument.ui.view.main.profile.viewmodel.ProfileViewModel
import com.natiqhaciyef.prodocument.ui.view.registration.RegistrationActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass


@AndroidEntryPoint
class ProfileFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProfileBinding = FragmentProfileBinding::inflate,
    override val viewModelClass: KClass<ProfileViewModel> = ProfileViewModel::class
) : BaseFragment<FragmentProfileBinding, ProfileViewModel, ProfileContract.ProfileState, ProfileContract.ProfileEvent, ProfileContract.ProfileEffect>() {
    private var adapter: AccountParametersAdapter? = null
    private val darkModeManager = DarkModeManager(requireContext())

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
        adapter = AccountParametersAdapter(this, list, darkModeManager.getCurrentMode())
        adapter?.onClickAction = { title -> adapterClickNavigation(title) }
        adapter?.switchIconClickAction = {
            darkModeManager.updateCurrentMode()
            navigate(com.natiqhaciyef.prodocument.R.id.profileFragment)
            darkModeManager.changeModeToggle()
        }

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
                viewModel.postEvent(
                    ProfileContract.ProfileEvent.GetAllSupportedLanguages(
                        requireContext()
                    )
                )
            }

            requireContext().getString(R.string.logout).lowercase() -> {
                LogOutFragment { onLogOutClick() }.show(
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

    private fun onLogOutClick() {
        navigate(requireActivity(), RegistrationActivity::class.java, true)
    }

    override fun onPause() {
        super.onPause()
        viewModel.postEvent(ProfileContract.ProfileEvent.ClearState)
    }

    companion object{
        fun settingEnumToNavigation(settings: Settings) = when(settings){
            Settings.PERSONAL_INFO -> { com.natiqhaciyef.prodocument.R.id.personalInfoFragment }
            Settings.PREFERENCE -> { com.natiqhaciyef.prodocument.R.id.preferencesFragment }
            Settings.SECURITY -> { com.natiqhaciyef.prodocument.R.id.securityFragment }
            Settings.PAYMENT_HISTORY -> { com.natiqhaciyef.prodocument.R.id.paymentHistoryFragment }
            Settings.CATEGORY_GRAPH -> { com.natiqhaciyef.prodocument.R.id.categoryGraphFragment }
            Settings.HELP_CENTER -> { com.natiqhaciyef.prodocument.R.id.helpCenterFragment }
            Settings.ABOUT_PROSCAN -> { com.natiqhaciyef.prodocument.R.id.aboutProscanFragment }
            else -> { null }
        }

        fun stringToNavigation(str: String) = when(str){
            Settings.PERSONAL_INFO.name -> { com.natiqhaciyef.prodocument.R.id.personalInfoFragment }
            Settings.PREFERENCE.name -> { com.natiqhaciyef.prodocument.R.id.preferencesFragment }
            Settings.SECURITY.name -> { com.natiqhaciyef.prodocument.R.id.securityFragment }
            Settings.PAYMENT_HISTORY.name -> { com.natiqhaciyef.prodocument.R.id.paymentHistoryFragment }
            Settings.CATEGORY_GRAPH.name -> { com.natiqhaciyef.prodocument.R.id.categoryGraphFragment }
            Settings.HELP_CENTER.name -> { com.natiqhaciyef.prodocument.R.id.helpCenterFragment }
            Settings.ABOUT_PROSCAN.name -> { com.natiqhaciyef.prodocument.R.id.aboutProscanFragment }
            else -> { null }
        }
    }
}