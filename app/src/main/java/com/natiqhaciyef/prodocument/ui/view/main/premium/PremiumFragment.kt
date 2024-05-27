package com.natiqhaciyef.prodocument.ui.view.main.premium

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.model.mapped.MappedSubscriptionModel
import com.natiqhaciyef.prodocument.databinding.FragmentPremiumBinding
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.home.options.scan.adapter.ScanViewPagerAdapter
import com.natiqhaciyef.prodocument.ui.view.main.premium.contract.PremiumContract
import com.natiqhaciyef.prodocument.ui.view.main.premium.viewmodel.PremiumViewModel
import com.natiqhaciyef.prodocument.ui.view.onboarding.behaviour.ZoomOutPageTransformer
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class PremiumFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPremiumBinding = FragmentPremiumBinding::inflate,
    override val viewModelClass: KClass<PremiumViewModel> = PremiumViewModel::class
) : BaseFragment<FragmentPremiumBinding, PremiumViewModel, PremiumContract.PremiumState, PremiumContract.PremiumEvent, PremiumContract.PremiumEffect>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        config()
    }

    override fun onStateChange(state: PremiumContract.PremiumState) {
        when{
            state.isLoading -> {
                changeVisibilityOfProgressBar(true)
            }
            else -> {
                changeVisibilityOfProgressBar()

                if (state.subscriptions != null){
                    viewPagerConfiguration(state.subscriptions!!)
                }
            }
        }
    }

    override fun onEffectUpdate(effect: PremiumContract.PremiumEffect) {

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

    private fun config() {
        (activity as MainActivity).also {
            it.binding.bottomNavBar.visibility = View.VISIBLE
            it.binding.materialToolbar.visibility = View.VISIBLE
            it.binding.appbarLayout.visibility = View.VISIBLE
            it.binding.materialToolbar.setTitleToolbar(getString(R.string.proscan))
            it.binding.materialToolbar.changeVisibility(View.VISIBLE)
            it.binding.materialToolbar.setVisibilityOptionsMenu(View.GONE)
            it.binding.materialToolbar.setVisibilitySearch(View.GONE)
            it.binding.materialToolbar.setVisibilityToolbar(View.VISIBLE)
        }

        viewModel.postEvent(PremiumContract.PremiumEvent.GetAllSubscriptionPlans)
    }

    private fun viewPagerConfiguration(list: List<MappedSubscriptionModel>){
        val plansFragmentList = mutableListOf<SubscriptionFragment>()
        for (subscriptionModel in list){
            plansFragmentList.add(SubscriptionFragment(subscriptionModel = subscriptionModel))
        }
        val adapter = ScanViewPagerAdapter(plansFragmentList, this@PremiumFragment)
        binding.subscriptionViewPager.adapter = adapter
        binding.subscriptionViewPager.setPageTransformer(ZoomOutPageTransformer())
    }
}