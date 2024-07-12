package com.natiqhaciyef.prodocument.ui.view.main.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.natiqhaciyef.common.constants.FORMATTED_NUMBER_TWO
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.constants.TWO_HUNDRED
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.common.model.Currency
import com.natiqhaciyef.common.model.mapped.MappedSubscriptionModel
import com.natiqhaciyef.common.model.payment.MappedPaymentChequeModel
import com.natiqhaciyef.common.model.payment.MappedPaymentModel
import com.natiqhaciyef.common.model.payment.MappedPaymentModel.Companion.toMappedPick
import com.natiqhaciyef.common.model.payment.PaymentResultType
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.databinding.FragmentPaymentDetailsBinding
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_CHEQUE_PAYMENT
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_PAYMENT
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_SUBSCRIPTION_PLAN
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.payment.contract.PaymentContract
import com.natiqhaciyef.prodocument.ui.view.main.payment.viewmodel.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class PaymentDetailsFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPaymentDetailsBinding = FragmentPaymentDetailsBinding::inflate,
    override val viewModelClass: KClass<PaymentViewModel> = PaymentViewModel::class
) : BaseFragment<FragmentPaymentDetailsBinding, PaymentViewModel, PaymentContract.PaymentState, PaymentContract.PaymentEvent, PaymentContract.PaymentEffect>() {
    private var chequeModel: MappedPaymentChequeModel? = null
    private var pickedPlan: MappedSubscriptionModel? = null
    private var paymentModel: MappedPaymentModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: PaymentDetailsFragmentArgs by navArgs()
        chequeModel = args.datasetBundle.getParcelable(BUNDLE_CHEQUE_PAYMENT)
        pickedPlan = args.datasetBundle.getParcelable(BUNDLE_SUBSCRIPTION_PLAN)
        paymentModel = args.datasetBundle.getParcelable(BUNDLE_PAYMENT)

        activityConfig()
        if (chequeModel != null && pickedPlan != null && paymentModel != null){
            planDetailsConfig(chequeModel= chequeModel!!, plan = pickedPlan!!, payment = paymentModel!!)
        }
    }

    override fun onStateChange(state: PaymentContract.PaymentState) {
        when {
            state.isLoading -> {
                changeVisibilityOfProgressBar(true)
                errorResultConfig()
            }

            isIdleState(state) -> {
                changeVisibilityOfProgressBar()
                errorResultConfig(true)
            }

            else -> {
                changeVisibilityOfProgressBar()
                errorResultConfig()

                if (chequeModel != null && state.paymentResult != null) {
                    if (state.paymentResult!!.resultCode in TWO_HUNDRED..TWO_HUNDRED_NINETY_NINE)
                        confirmButtonAction(chequeModel!!)
                    else
                        confirmButtonAction(chequeModel!!.copy(paymentResult = PaymentResultType.FAIL))
                }

                if (state.paymentMethodsList != null){
                    paymentBottomSheetConfig(state.paymentMethodsList!!)
                }
            }
        }
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

    private fun errorResultConfig(isVisible: Boolean = false){
        with(binding){
            notFoundLayout.visibility = if (isVisible) View.VISIBLE else View.GONE
            uiLayout.visibility = if (isVisible) View.GONE else View.VISIBLE

            notFoundDescription.text = getString(com.natiqhaciyef.common.R.string.files_loading_error_description_result)
            notFoundTitle.text = SOMETHING_WENT_WRONG
        }
    }

    private fun activityConfig() {
        (activity as MainActivity).also {
            with(it.binding) {
                bottomNavBar.visibility = View.GONE

                materialToolbar.apply {
                    visibility = View.VISIBLE
                    setTitleToolbar(getString(com.natiqhaciyef.common.R.string.review_summary))
                    changeVisibility(View.VISIBLE)
                    appIconVisibility(View.GONE)
                    setVisibilitySearch(View.GONE)
                    setVisibilityOptionsMenu(View.GONE)
                    setIconToOptions(com.natiqhaciyef.common.R.drawable.toolbar_scan_icon)
                    setVisibilityToolbar(View.VISIBLE)
                }
            }
        }
    }

    private fun planDetailsConfig(plan: MappedSubscriptionModel, chequeModel: MappedPaymentChequeModel, payment: MappedPaymentModel) {
        val pickedPayment = payment.toMappedPick()
        val currency = Currency.stringToSign(chequeModel.paymentDetails.currency)
        val fee = FORMATTED_NUMBER_TWO.format(chequeModel.subscriptionDetails.fee)
        val total = FORMATTED_NUMBER_TWO.format(chequeModel.totalAmount)
        val price = FORMATTED_NUMBER_TWO.format(chequeModel.subscriptionDetails.price)
        val perTimeType = chequeModel.subscriptionDetails.expirationTimeType.title
        val perTime = chequeModel.subscriptionDetails.expirationTime

        with(binding) {
            subscriptionPlanDetails.text = plan.title.title
            subscriptionPlanDateDetails.text = "$perTime $perTimeType"
            planAmountDetails.text = getString(com.natiqhaciyef.common.R.string.payment_currency, currency, price)
            taxAmountDetails.text = getString(com.natiqhaciyef.common.R.string.payment_currency, currency, fee)

            totalAmountDetails.text = getString(com.natiqhaciyef.common.R.string.payment_currency, currency, total)
            paymentTypeImage.setImageResource(pickedPayment.image)
            maskedCardNumber.text = pickedPayment.maskedCardNumber
            confirmButton.setOnClickListener { viewModel.postEvent(PaymentContract.PaymentEvent.PayForPlan(chequeModel)) }
            changePaymentMethod.setOnClickListener { viewModel.postEvent(PaymentContract.PaymentEvent.GetAllStoredPaymentMethods) }
        }
    }

    private fun paymentBottomSheetConfig(list: List<MappedPaymentModel>){
        CustomPaymentMethodsFragment(list = list){
            if (pickedPlan != null && chequeModel != null)
                planDetailsConfig(plan = pickedPlan!!, chequeModel = chequeModel!!, payment = it)
        }.show(this.childFragmentManager, CustomPaymentMethodsFragment::class.simpleName)
    }

    private fun confirmButtonAction(chequeModel: MappedPaymentChequeModel){
        val action = PaymentDetailsFragmentDirections.actionPaymentDetailsFragmentToPaymentResultFragment(chequeModel)
        navigate(action)
    }
}