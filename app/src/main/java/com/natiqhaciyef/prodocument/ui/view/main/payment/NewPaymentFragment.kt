package com.natiqhaciyef.prodocument.ui.view.main.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.core.widget.doOnTextChanged
import com.natiqhaciyef.common.constants.FOUR
import com.natiqhaciyef.common.constants.HUNDRED
import com.natiqhaciyef.common.constants.NINE
import com.natiqhaciyef.common.constants.SIXTEEN
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.constants.SPACE
import com.natiqhaciyef.common.constants.TEN
import com.natiqhaciyef.common.constants.TWO_HUNDRED
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
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
            state.isLoading -> binding.uiLayout.loadingState(true)

            isIdleState(state) -> binding.uiLayout.errorState(true)

            else -> {
                binding.uiLayout.successState()

                if (state.paymentResult?.resultCode in TWO_HUNDRED..TWO_HUNDRED_NINETY_NINE) {
                    addButtonClickAction()
                }
            }
        }
    }

    private fun activityConfig() {
        (activity as MainActivity).also {
            with(it.binding) {
                bottomNavBar.visibility = View.GONE
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
            cardNumberFieldInput.setMaxLength(SIXTEEN)
            cvvFieldInput.setMaxLength(FOUR)
            cardNumberFieldInput.listenUserInputWithAddTextWatcher(CardNumberMaskingListener(cardNumberField))
            expireDateFieldInput.addTextChangedListener(ExpireMaskingListener(expirationInput))
            cardHolderFieldInput.listenUserInput { text, start, before, count ->
                cardHolderNameInput.text = text
            }
        }
    }

    private fun buttonConfig() {
        with(binding) {
            addButton.setOnClickListener {
                if (cardHolderNameInput.text.contains(SPACE) &&
                    cardNumberField.text.isNotEmpty() &&
                    expirationInput.text.isNotEmpty() &&
                    cvvFieldInput.getInputResult().isNotEmpty()
                )
                    addButtonClickEvent(
                        cardHolderName = cardHolderNameInput.text.toString(),
                        cardNumber = cardNumberField.text.toString(),
                        expireDate = expirationInput.text.toString(),
                        cvv = cvvFieldInput.getInputResult()
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
                    merchantId = "${TEN}${NINE}".toInt(),
                    paymentType = PaymentTypes.CARD,
                    paymentMethod = PaymentMethods.UNKNOWN,
                    paymentDetails = PaymentDetails(
                        cardHolder = cardHolderName,
                        cardNumber = cardNumber,
                        expireDate = expireDate,
                        cvv = cvv,
                        currency = Currency.USD.name
                    )
                )
            )
        )
    }

    private fun addButtonClickAction() {
        navigateBack()
    }
}