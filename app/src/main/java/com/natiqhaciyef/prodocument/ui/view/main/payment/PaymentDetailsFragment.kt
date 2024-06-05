package com.natiqhaciyef.prodocument.ui.view.main.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
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
    private var pickedPlan: MappedSubscriptionModel? = null
    private var pickedPaymentModel: MappedPaymentModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: PaymentDetailsFragmentArgs by navArgs()
        pickedPlan = args.datasetBundle.getParcelable(BundleConstants.BUNDLE_SUBSCRIPTION_PLAN)
        pickedPaymentModel = args.datasetBundle.getParcelable(BundleConstants.BUNDLE_PAYMENT)

        activityConfig()
        if (pickedPlan != null && pickedPaymentModel != null)
            planDetailsConfig(pickedPlan!!, pickedPaymentModel!!)
    }

    override fun onStateChange(state: PaymentContract.PaymentState) {
        when {
            state.isLoading -> {

            }

            else -> {
                if (state.cheque != null)
                    confirmButtonAction(state.cheque!!)
                else
                    println("Fake")
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

    private fun planDetailsConfig(
        pickedPlan: MappedSubscriptionModel,
        paymentModel: MappedPaymentModel
    ) {
        val tax = String.format("%.2f", (pickedPlan.price * 0.05))
        val totalPrice = tax.toDouble() + pickedPlan.price
        val pickedPayment = paymentModel.toMappedPick()
        val currency = Currency.stringToSign(paymentModel.paymentDetails.currency)

        with(binding) {
            subscriptionPlanDetails.text = pickedPlan.title.title
            subscriptionPlanDateDetails.text = "${pickedPlan.perTime} ${pickedPlan.timeType.title}"
            planAmountDetails.text = "${currency}${pickedPlan.price}"
            taxAmountDetails.text = "${currency}${tax}"

            totalAmountDetails.text = "${currency}${totalPrice}"
            paymentTypeImage.setImageResource(pickedPayment.image)
            maskedCardNumber.text = pickedPayment.maskedCardNumber
            confirmButtonEvent(paymentModel)
        }
    }

    private fun confirmButtonEvent(paymentModel: MappedPaymentModel){
        binding.confirmButton.setOnClickListener {
            viewModel.postEvent(PaymentContract.PaymentEvent.PayForPlan(paymentModel = paymentModel))
        }
    }

    private fun confirmButtonAction(chequeModel: MappedPaymentChequeModel){
        // navigate payment cheque screen
        println(chequeModel)
    }
}