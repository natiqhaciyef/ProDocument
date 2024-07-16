package com.natiqhaciyef.prodocument.ui.view.main.profile.params.security

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentSecurityBinding
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.profile.contract.ProfileContract
import com.natiqhaciyef.common.model.ParamsUIModel
import com.natiqhaciyef.core.base.ui.BaseRecyclerHolderStatefulFragment
import com.natiqhaciyef.uikit.adapter.SecurityParamsAdapter
import com.natiqhaciyef.prodocument.ui.view.main.profile.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass


@AndroidEntryPoint
class SecurityFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSecurityBinding = FragmentSecurityBinding::inflate,
    override val viewModelClass: KClass<ProfileViewModel> = ProfileViewModel::class
) : BaseRecyclerHolderStatefulFragment<
        FragmentSecurityBinding, ProfileViewModel, ParamsUIModel, SecurityParamsAdapter,
        ProfileContract.ProfileState, ProfileContract.ProfileEvent, ProfileContract.ProfileEffect>() {
    override var adapter: SecurityParamsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityConfig()
        viewModel.postEvent(ProfileContract.ProfileEvent.GetSecurityParams(requireContext()))
    }

    override fun onStateChange(state: ProfileContract.ProfileState) {
        when {
            state.isLoading -> binding.uiLayout.loadingState(true)

            isIdleState(state) -> binding.uiLayout.errorState(true)

            else -> {
                binding.uiLayout.successState()

                if (state.paramsUIModelList != null) {
                    recyclerViewConfig(state.paramsUIModelList!!)
                }
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
                    setTitleToolbar(getString(com.natiqhaciyef.common.R.string.security))
                    appIconVisibility(View.VISIBLE)
                    setVisibilitySearch(View.GONE)
                    setVisibilityOptionsMenu(View.GONE)
                    changeAppIcon(com.natiqhaciyef.common.R.drawable.back_arrow_icon) {
                        onToolbarBackPressAction(bottomNavBar)
                    }
                }
            }
        }
    }

    private fun onToolbarBackPressAction(bnw: BottomNavigationView) {
        bnw.visibility = View.VISIBLE
        navigate(R.id.profileFragment)
    }

    override fun recyclerViewConfig(list: List<ParamsUIModel>) {
        adapter = SecurityParamsAdapter((requireActivity() as MainActivity), list.toMutableList())

        with(binding) {
            recyclerSecurityParamsView.adapter = adapter
            recyclerSecurityParamsView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }
}