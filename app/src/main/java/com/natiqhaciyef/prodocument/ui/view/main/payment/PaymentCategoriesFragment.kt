package com.natiqhaciyef.prodocument.ui.view.main.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.model.mapped.MappedSubscriptionModel
import com.natiqhaciyef.common.model.payment.MappedPaymentModel
import com.natiqhaciyef.core.base.ui.BaseRecyclerHolderStatefulFragment
import com.natiqhaciyef.prodocument.databinding.FragmentPaymentCategoriesBinding
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_CHEQUE_PAYMENT
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_PAYMENT
import com.natiqhaciyef.prodocument.ui.util.BUNDLE_SUBSCRIPTION_PLAN
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil.HOME_ROUTE
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.uikit.adapter.PaymentMethodsAdapter
import com.natiqhaciyef.prodocument.ui.view.main.payment.contract.PaymentContract
import com.natiqhaciyef.prodocument.ui.view.main.payment.viewmodel.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass


@AndroidEntryPoint
class PaymentCategoriesFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPaymentCategoriesBinding = FragmentPaymentCategoriesBinding::inflate,
    override val viewModelClass: KClass<PaymentViewModel> = PaymentViewModel::class
) : BaseRecyclerHolderStatefulFragment<
        FragmentPaymentCategoriesBinding, PaymentViewModel, MappedPaymentModel, PaymentMethodsAdapter,
        PaymentContract.PaymentState, PaymentContract.PaymentEvent, PaymentContract.PaymentEffect>() {
    override var adapter: PaymentMethodsAdapter? = null
    private var resourceBundle = bundleOf()
    private var paymentModel: MappedPaymentModel? = null
    private var pickedSubscriptionModel: MappedSubscriptionModel? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.postEvent(PaymentContract.PaymentEvent.GetAllStoredPaymentMethods)
        activityConfig()

        val arg: PaymentCategoriesFragmentArgs by navArgs()
        resourceBundle = arg.datasetBundle
        pickedSubscriptionModel = resourceBundle.getParcelable(BUNDLE_SUBSCRIPTION_PLAN)

        binding.addNewPaymentMethodButton.setOnClickListener { addNewPaymentMethodButtonClick() }
    }

    override fun onStateChange(state: PaymentContract.PaymentState) {
        println(state)
        when {
            state.isLoading -> binding.uiLayout.loadingState(true)

            isIdleState(state) -> binding.uiLayout.errorState(true)

            else -> {
                binding.uiLayout.successState()

                if (state.paymentMethodsList != null) {
                    if (state.paymentMethodsList!!.isNotEmpty()) {
                        recyclerViewConfig(state.paymentMethodsList!!)
                        emptyListConfig(false)
                    }else
                        emptyListConfig(true)
                }

                if (state.cheque != null) {
                    resourceBundle.putParcelable(BUNDLE_CHEQUE_PAYMENT, state.cheque!!)
                    resourceBundle.putParcelable(BUNDLE_PAYMENT, paymentModel)
                    val action =
                        PaymentCategoriesFragmentDirections
                            .actionPaymentCategoriesFragmentToPaymentDetailsFragment(resourceBundle)
                    navigate(action)
                }
            }
        }
    }

    override fun onEffectUpdate(effect: PaymentContract.PaymentEffect) {

    }

    private fun emptyListConfig(isVisible: Boolean = false) {
        binding.emptyListLayout.visibility =
            if (isVisible) View.VISIBLE else View.GONE
    }

    private fun activityConfig() {
        (activity as MainActivity).also {
            with(it.binding) {
                bottomNavBar.visibility = View.GONE

                materialToolbar.apply {
                    visibility = View.VISIBLE
                    setTitleToolbar(getString(com.natiqhaciyef.common.R.string.select_payment_method))
                    changeVisibility(View.VISIBLE)
                    appIconVisibility(View.GONE)
                    setVisibilitySearch(View.GONE)
                    setVisibilityOptionsMenu(View.VISIBLE)
                    setIconToOptions(com.natiqhaciyef.common.R.drawable.toolbar_scan_icon)
                    setVisibilityToolbar(View.VISIBLE)
                    optionSetOnClickListener { onScanIconClickAction() }
                    setNavigationOnClickListener { onToolbarBackPressAction(bottomNavBar) }
                }
            }
        }
    }

    private fun onToolbarBackPressAction(bnw: BottomNavigationView) {
        bnw.visibility = View.VISIBLE
        NavigationUtil.navigateByRouteTitle(this, HOME_ROUTE)
    }

    private fun onScanIconClickAction() {
        val action = PaymentCategoriesFragmentDirections
            .actionPaymentCategoriesFragmentToScanFragment(resourceBundle)
        navigate(action)
    }

    override fun recyclerViewConfig(list: List<MappedPaymentModel>) {
        adapter = PaymentMethodsAdapter(list = list.toMutableList())
        adapter?.onClickAction = {
            // navigate to details screen with loading screen
            paymentModel = it
            viewModel.postEvent(
                PaymentContract.PaymentEvent.GetPaymentData(
                    paymentModel = it,
                    pickedPlan = pickedSubscriptionModel?.token ?: EMPTY_STRING
                )
            )
        }

        binding.pickPaymentRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.pickPaymentRecyclerView.adapter = adapter

    }

    private fun addNewPaymentMethodButtonClick() {
        val action = PaymentCategoriesFragmentDirections
            .actionPaymentCategoriesFragmentToNewPaymentFragment()
        navigate(action)
    }
}