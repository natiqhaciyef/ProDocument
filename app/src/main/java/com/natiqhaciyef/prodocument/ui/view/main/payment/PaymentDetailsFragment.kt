package com.natiqhaciyef.prodocument.ui.view.main.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.natiqhaciyef.common.model.Currency
import com.natiqhaciyef.common.model.mapped.MappedSubscriptionModel
import com.natiqhaciyef.common.model.payment.MappedPaymentChequeModel
import com.natiqhaciyef.common.model.payment.MappedPaymentModel
import com.natiqhaciyef.common.model.payment.MappedPaymentModel.Companion.toMappedPick
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.databinding.FragmentPaymentDetailsBinding
import com.natiqhaciyef.prodocument.ui.util.BundleConstants
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
        chequeModel = args.datasetBundle.getParcelable(BundleConstants.BUNDLE_CHEQUE_PAYMENT)
        pickedPlan = args.datasetBundle.getParcelable(BundleConstants.BUNDLE_SUBSCRIPTION_PLAN)
        paymentModel = args.datasetBundle.getParcelable(BundleConstants.BUNDLE_PAYMENT)

        println(chequeModel)
        println(pickedPlan)
        println(paymentModel)

        activityConfig()
        if (chequeModel != null && pickedPlan != null && paymentModel != null){
            planDetailsConfig(chequeModel= chequeModel!!, plan = pickedPlan!!, payment = paymentModel!!)
        }
    }

    override fun onStateChange(state: PaymentContract.PaymentState) {
        when {
            state.isLoading -> {

            }

            else -> {
                if (state.cheque != null)
                    confirmButtonAction(state.cheque!!)
            }
        }
    }

    private fun activityConfig() {
        (activity as MainActivity).also {
            with(it.binding) {
                bottomNavBar.visibility = View.GONE
                appbarLayout.visibility = View.VISIBLE

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
        val fee = "%.2f".format(chequeModel.subscriptionDetails.fee)
        val total = "%.2f".format(chequeModel.totalAmount)
        val price = "%.2f".format(chequeModel.subscriptionDetails.price)
        val perTimeType = chequeModel.subscriptionDetails.expirationTimeType.title
        val perTime = chequeModel.subscriptionDetails.expirationTime

        with(binding) {
            subscriptionPlanDetails.text = plan.title.title
            subscriptionPlanDateDetails.text = "$perTime $perTimeType"
            planAmountDetails.text = "${currency}${price}"
            taxAmountDetails.text = "${currency}${fee}"

            totalAmountDetails.text = "${currency}${total}"
            paymentTypeImage.setImageResource(pickedPayment.image)
            maskedCardNumber.text = pickedPayment.maskedCardNumber
            binding.confirmButton.setOnClickListener {
                viewModel.postEvent(PaymentContract.PaymentEvent.PayForPlan)
            }
        }
    }

    private fun confirmButtonAction(chequeModel: MappedPaymentChequeModel){
        // navigate payment cheque screen
        val action = PaymentDetailsFragmentDirections.actionPaymentDetailsFragmentToPaymentResultFragment(chequeModel)
        navigate(action)
    }
}