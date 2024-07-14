package com.natiqhaciyef.prodocument.ui.view.main.premium

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.model.mapped.MappedSubscriptionModel
import com.natiqhaciyef.prodocument.databinding.FragmentPremiumBinding
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.premium.contract.PremiumContract
import com.natiqhaciyef.prodocument.ui.view.main.premium.viewmodel.PremiumViewModel
import com.natiqhaciyef.prodocument.ui.view.onboarding.behaviour.ZoomOutPageTransformer
import com.natiqhaciyef.uikit.adapter.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class PremiumFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPremiumBinding = FragmentPremiumBinding::inflate,
    override val viewModelClass: KClass<PremiumViewModel> = PremiumViewModel::class
) : BaseFragment<FragmentPremiumBinding, PremiumViewModel, PremiumContract.PremiumState, PremiumContract.PremiumEvent, PremiumContract.PremiumEffect>() {
    private val plansFragmentList = mutableListOf<SubscriptionFragment>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        config()
    }

    override fun onStateChange(state: PremiumContract.PremiumState) {
        when{
            state.isLoading -> binding.uiLayout.loadingState(true)

            state.subscriptions.isNullOrEmpty() -> {
                viewModel.postEvent(PremiumContract.PremiumEvent.GetAllSubscriptionPlans)
            }

            isIdleState(state) -> binding.uiLayout.errorState(true)

            else -> {
                binding.uiLayout.successState()

                if (state.subscriptions != null){
                    viewPagerConfiguration(state.subscriptions!!)
                }
            }
        }
    }

    private fun config() {
        (activity as MainActivity).also {
            it.binding.bottomNavBar.visibility = View.VISIBLE
            it.binding.materialToolbar.visibility = View.VISIBLE
            it.binding.materialToolbar.setTitleToolbar(getString(R.string.proscan))
            it.binding.materialToolbar.changeVisibility(View.VISIBLE)
            it.binding.materialToolbar.setVisibilityOptionsMenu(View.GONE)
            it.binding.materialToolbar.setVisibilitySearch(View.GONE)
            it.binding.materialToolbar.setVisibilityToolbar(View.VISIBLE)
        }
    }

    private fun viewPagerConfiguration(list: List<MappedSubscriptionModel>){
        for (subscriptionModel in list){
            plansFragmentList.add(SubscriptionFragment(subscription = subscriptionModel))
        }
        val adapter = ViewPagerAdapter(plansFragmentList, this)
        binding.subscriptionViewPager.adapter = adapter
        binding.subscriptionViewPager.setPageTransformer(ZoomOutPageTransformer())
    }
}