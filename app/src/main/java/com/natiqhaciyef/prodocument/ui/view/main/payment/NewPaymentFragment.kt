package com.natiqhaciyef.prodocument.ui.view.main.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.natiqhaciyef.common.helpers.CardNumberMaskingListener
import com.natiqhaciyef.common.helpers.ExpireMaskingListener
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.databinding.FragmentNewPaymentBinding
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.payment.contract.PaymentContract
import com.natiqhaciyef.prodocument.ui.view.main.payment.viewmodel.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class NewPaymentFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNewPaymentBinding = FragmentNewPaymentBinding::inflate,
    override val viewModelClass: KClass<PaymentViewModel> = PaymentViewModel::class
) : BaseFragment<FragmentNewPaymentBinding, PaymentViewModel, PaymentContract.PaymentState, PaymentContract.PaymentEvent, PaymentContract.PaymentEffect>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityConfig()
        inputConfig()
    }

    override fun onStateChange(state: PaymentContract.PaymentState) {
        when {
            state.isLoading -> {

            }

            else -> {

            }
        }
    }

    private fun activityConfig() {
        (activity as MainActivity).also {
            with(it.binding) {
                bottomNavBar.visibility = View.VISIBLE
                appbarLayout.visibility = View.VISIBLE

                materialToolbar.apply {
                    visibility = View.VISIBLE
                    setTitleToolbar(getString(com.natiqhaciyef.common.R.string.add_new_payment))
                    changeVisibility(View.VISIBLE)
                    appIconVisibility(View.GONE)
                    setVisibilitySearch(View.GONE)
                    setVisibilityOptionsMenu(View.GONE)
                    setVisibilityToolbar(View.VISIBLE)
                    optionSetOnClickListener { onCloseButtonClickAction() }
                }
            }
        }

        viewModel.postEvent(PaymentContract.PaymentEvent.GetAllStoredPaymentMethods)
    }

    private fun onCloseButtonClickAction() {

    }

    private fun inputConfig() {
        with(binding) {
            cardNumberFieldInput.addTextChangedListener(CardNumberMaskingListener(cardNumberField))
            expireDateFieldInput.addTextChangedListener(ExpireMaskingListener(expirationInput))
        }
    }
}