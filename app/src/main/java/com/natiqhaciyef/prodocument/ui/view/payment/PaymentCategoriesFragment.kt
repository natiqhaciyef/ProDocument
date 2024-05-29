package com.natiqhaciyef.prodocument.ui.view.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.databinding.FragmentPaymentCategoriesBinding
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.premium.contract.PremiumContract
import com.natiqhaciyef.prodocument.ui.view.payment.contract.PaymentContract
import com.natiqhaciyef.prodocument.ui.view.payment.viewmodel.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass


@AndroidEntryPoint
class PaymentCategoriesFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPaymentCategoriesBinding = FragmentPaymentCategoriesBinding::inflate,
    override val viewModelClass: KClass<PaymentViewModel> = PaymentViewModel::class
) : BaseFragment<FragmentPaymentCategoriesBinding, PaymentViewModel, PaymentContract.PaymentState, PaymentContract.PaymentEvent, PaymentContract.PaymentEffect>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityConfig()
    }

    override fun onStateChange(state: PaymentContract.PaymentState) {
        when {
            state.isLoading -> {
                changeVisibilityOfProgressBar(true)
            }

            else -> {
                changeVisibilityOfProgressBar()

            }
        }
    }

    override fun onEffectUpdate(effect: PaymentContract.PaymentEffect) {

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
            it.binding.appbarLayout.visibility = View.VISIBLE
            it.binding.materialToolbar.setTitleToolbar(getString(com.natiqhaciyef.common.R.string.proscan))
            it.binding.materialToolbar.changeVisibility(View.VISIBLE)
            it.binding.materialToolbar.setVisibilityOptionsMenu(View.GONE)
            it.binding.materialToolbar.setVisibilitySearch(View.GONE)
            it.binding.materialToolbar.setVisibilityToolbar(View.VISIBLE)
        }

        viewModel.postEvent(PaymentContract.PaymentEvent.GetAllStoredPaymentMethods)
    }
}