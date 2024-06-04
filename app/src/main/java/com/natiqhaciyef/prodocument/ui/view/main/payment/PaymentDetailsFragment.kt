package com.natiqhaciyef.prodocument.ui.view.main.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.natiqhaciyef.common.model.mapped.MappedSubscriptionModel
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityConfig()

        val args: PaymentDetailsFragmentArgs by navArgs()
        pickedPlan = args.datasetBundle.getParcelable(BundleConstants.BUNDLE_SUBSCRIPTION_PLAN)
    }

    override fun onStateChange(state: PaymentContract.PaymentState) {
        when{
            state.isLoading -> {

            }

            else -> {}
        }
    }

    private fun activityConfig(){
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


}