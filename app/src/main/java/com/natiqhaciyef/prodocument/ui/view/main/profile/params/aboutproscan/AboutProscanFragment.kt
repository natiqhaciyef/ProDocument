package com.natiqhaciyef.prodocument.ui.view.main.profile.params.aboutproscan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.natiqhaciyef.common.helpers.loadImage
import com.natiqhaciyef.common.model.ProScanInfoModel
import com.natiqhaciyef.common.model.ProscanSectionModel
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentAboutProscanBinding
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.profile.contract.ProfileContract
import com.natiqhaciyef.prodocument.ui.view.main.profile.params.aboutproscan.adapter.SectionsAdapter
import com.natiqhaciyef.prodocument.ui.view.main.profile.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class AboutProscanFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAboutProscanBinding = FragmentAboutProscanBinding::inflate,
    override val viewModelClass: KClass<ProfileViewModel> = ProfileViewModel::class
) : BaseFragment<FragmentAboutProscanBinding, ProfileViewModel, ProfileContract.ProfileState, ProfileContract.ProfileEvent, ProfileContract.ProfileEffect>() {
    private var adapter: SectionsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityConfig()
        postEvent(ProfileContract.ProfileEvent.GetProscanInfo)
    }

    override fun onStateChange(state: ProfileContract.ProfileState) {
        when {
            state.isLoading -> {}

            else -> {

                if (state.proscanInfo != null) {
                    topConfig(state.proscanInfo!!)
                    holdCurrentState(state)
                    postEvent(ProfileContract.ProfileEvent.GetProscanSections)
                }

                if (state.proscanSections != null) {
                    sectionsRecyclerConfig(state.proscanSections!!)
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
                    setTitleToolbar(getString(com.natiqhaciyef.common.R.string.about_proscan))
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

    private fun topConfig(proScanInfoModel: ProScanInfoModel) {
        with(binding) {
            proscanImage.loadImage(proScanInfoModel.icon)
            titleTagAndVersion.text =
                requireContext().getString(
                    com.natiqhaciyef.common.R.string.proscan_title_and_version,
                    proScanInfoModel.title,
                    proScanInfoModel.version
                )
        }
    }

    private fun sectionsRecyclerConfig(list: List<ProscanSectionModel>) {
        adapter = SectionsAdapter(list.toMutableList())

        with(binding) {
            recyclerDetailsView.adapter = adapter
            recyclerDetailsView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter?.onClick = {
                // add action
            }
        }
    }
}