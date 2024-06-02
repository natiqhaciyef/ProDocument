package com.natiqhaciyef.prodocument.ui.view.main.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.natiqhaciyef.common.helpers.CardNumberMaskingListener
import com.natiqhaciyef.common.helpers.ExpireMaskingListener
import com.natiqhaciyef.common.model.Currency
import com.natiqhaciyef.common.model.payment.MappedPaymentModel
import com.natiqhaciyef.common.model.payment.PaymentDetails
import com.natiqhaciyef.common.model.payment.PaymentMethods
import com.natiqhaciyef.common.model.payment.PaymentTypes
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
        buttonConfig()
    }

    override fun onStateChange(state: PaymentContract.PaymentState) {
        when {
            state.isLoading -> {

            }

            else -> {
                if (state.paymentResult?.resultCode in 200..299){
                    addButtonClickAction()
                }
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
            cardHolderFieldInput.doOnTextChanged { text, start, before, count ->
                cardHolderNameInput.text = text
            }
        }
    }

    private fun buttonConfig() {
        with(binding) {
            addButton.setOnClickListener {
                if (cardHolderNameInput.text.contains(" ") &&
                    cardNumberField.text.isNotEmpty() &&
                    expirationInput.text.isNotEmpty() &&
                    cvvFieldInput.text.isNotEmpty()
                )
                    addButtonClickEvent(
                        cardHolderName = cardHolderNameInput.text.toString(),
                        cardNumber = cardNumberField.text.toString(),
                        expireDate = expirationInput.text.toString(),
                        cvv = cvvFieldInput.text.toString()
                    )
            }
        }
    }

    private fun addButtonClickEvent(
        cardHolderName: String,
        cardNumber: String,
        expireDate: String,
        cvv: String
    ) {
        viewModel.postEvent(
            PaymentContract.PaymentEvent.AddNewPaymentMethod(
                MappedPaymentModel(
                    merchantId = 0,
                    paymentType = PaymentTypes.CARD,
                    paymentMethod = PaymentMethods.UNKNOWN,
                    paymentDetails = PaymentDetails(
                        cardHolder = cardHolderName,
                        cardNumber = cardNumber,
                        expireDate = expireDate,
                        cvv = cvv,
                        currency = Currency.DEFAULT.name
                    )
                )
            )
        )
    }

    private fun addButtonClickAction(){
        // navigate to payment list and update screen
    }
}