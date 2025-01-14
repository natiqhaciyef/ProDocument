package com.natiqhaciyef.prodocument.ui.view.main.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.fragment.navArgs
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.SPACE
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.model.payment.MappedPaymentChequeModel
import com.natiqhaciyef.common.model.payment.PaymentResultType
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.core.model.FileTypes.PNG
import com.natiqhaciyef.prodocument.BuildConfig
import com.natiqhaciyef.prodocument.databinding.FragmentPaymentResultBinding
import com.natiqhaciyef.uikit.manager.FileManager.createAndShareFile
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.payment.contract.PaymentContract
import com.natiqhaciyef.prodocument.ui.view.main.payment.viewmodel.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass


@AndroidEntryPoint
class PaymentResultFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPaymentResultBinding = FragmentPaymentResultBinding::inflate,
    override val viewModelClass: KClass<PaymentViewModel> = PaymentViewModel::class
) : BaseFragment<FragmentPaymentResultBinding, PaymentViewModel, PaymentContract.PaymentState, PaymentContract.PaymentEvent, PaymentContract.PaymentEffect>() {
    private var mappedCheque: MappedPaymentChequeModel? = null
    private var payload: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: PaymentResultFragmentArgs by navArgs()
        mappedCheque = args.cheque

        mappedCheque?.let { initConfig(it) }
        activityConfig()
    }

    override fun onStateChange(state: PaymentContract.PaymentState) {
        when {
            state.isLoading -> binding.uiLayout.loadingState(true)

            else -> {
                binding.uiLayout.successState()

                if (!state.chequePayload.isNullOrEmpty())
                    payload = state.chequePayload
            }
        }
    }

    private fun activityConfig() {
        (activity as MainActivity).also {
            with(it.binding) {
                bottomNavBar.visibility = View.GONE

                materialToolbar.apply {
                    visibility = View.GONE
                    setTitleToolbar(SPACE)
                    changeVisibility(View.GONE)
                    appIconVisibility(View.GONE)
                    setVisibilitySearch(View.GONE)
                    setVisibilityOptionsMenu(View.GONE)
                    setVisibilityToolbar(View.GONE)
                }
            }
        }
    }

    private fun initConfig(chequeModel: MappedPaymentChequeModel){
        viewModel.postEvent(PaymentContract.PaymentEvent.GetChequePdf(chequeId = chequeModel.checkId))

        with(binding){
            chequeResultDataConfig(chequeModel.paymentResult)
            continueButton.setOnClickListener {
                NavigationUtil.navigateByRouteTitle(this@PaymentResultFragment, NavigationUtil.HOME_ROUTE)
            }
            chequeTitle.setOnClickListener { shareCheque(payload ?: EMPTY_STRING) }
            viewCheque.setOnClickListener { shareCheque(payload ?: EMPTY_STRING) }
        }
    }

    private fun chequeResultDataConfig(result: PaymentResultType){
        with(binding){
            when(result){
                PaymentResultType.FAIL -> {
                    resultTitleText.text = getString(com.natiqhaciyef.common.R.string.payment_failed)
                    resultIcon.setImageResource(com.natiqhaciyef.common.R.drawable.fail_result_type_icon)
                }

                PaymentResultType.SUCCESS -> {
                    resultTitleText.text = getString(com.natiqhaciyef.common.R.string.payment_succeed)
                    resultIcon.setImageResource(com.natiqhaciyef.common.R.drawable.success_result_type_icon)
                }

                PaymentResultType.REVERSED -> {
                    resultTitleText.text = getString(com.natiqhaciyef.common.R.string.payment_reversed)
                    resultIcon.setImageResource(com.natiqhaciyef.common.R.drawable.swap_reverse_result_type_icon)
                }

                else -> {
                    resultTitleText.text = getString(com.natiqhaciyef.common.R.string.payment_waiting)
                    resultIcon.setImageResource(com.natiqhaciyef.common.R.drawable.inform_result_type_icon)
                }
            }

        }
    }

    private fun shareCheque(image: String){
        createAndShareFile(
            applicationId = BuildConfig.APPLICATION_ID,
            material = MappedMaterialModel(
                id = chequeId,
                image = image,
                title = title,
                description = null,
                createdDate = getNow(),
                type = PNG,
                url = image.toUri(),
            )
        )
    }


    companion object{
        private const val chequeId = "chequeResult"
        private const val title = "Payment receipt"
    }
}