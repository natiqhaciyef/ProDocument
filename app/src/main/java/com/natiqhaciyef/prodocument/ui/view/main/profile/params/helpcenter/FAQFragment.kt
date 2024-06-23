package com.natiqhaciyef.prodocument.ui.view.main.profile.params.helpcenter

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.natiqhaciyef.common.model.FaqModel
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.databinding.FragmentFaqBinding
import com.natiqhaciyef.prodocument.ui.view.main.profile.contract.ProfileContract
import com.natiqhaciyef.prodocument.ui.view.main.profile.params.helpcenter.adapter.FaqAdapter
import com.natiqhaciyef.prodocument.ui.view.main.profile.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class FAQFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFaqBinding = FragmentFaqBinding::inflate,
    override val viewModelClass: KClass<ProfileViewModel> = ProfileViewModel::class
) : BaseFragment<FragmentFaqBinding, ProfileViewModel, ProfileContract.ProfileState, ProfileContract.ProfileEvent, ProfileContract.ProfileEffect>() {
    private var faqAdapter: FaqAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.postEvent(ProfileContract.ProfileEvent.GetFaqList)
        searchConfig()
    }

    override fun onStateChange(state: ProfileContract.ProfileState) {
        when {
            state.isLoading -> {

            }

            else -> {

                if (state.faqList != null) {

                }
            }
        }
    }

    override fun onEffectUpdate(effect: ProfileContract.ProfileEffect) {

    }

    private fun categoryRecyclerConfig() {
        with(binding) {

        }
    }

    private fun questionsRecyclerConfig(list: List<FaqModel>) {
        with(binding) {
//            faqAdapter = FaqAdapter(list.toMutableList())

        }
    }

    private fun searchConfig() {
        val search =
            binding.searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_button)
        search?.apply {
            layoutParams.width = 24
            layoutParams.height = 24
            imageTintList =
                ColorStateList.valueOf(resources.getColor(com.natiqhaciyef.common.R.color.grayscale_500))
        }
    }
}