package com.natiqhaciyef.prodocument.ui.view.main.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.natiqhaciyef.common.helpers.toDetails
import com.natiqhaciyef.common.model.mapped.MappedSubscriptionModel
import com.natiqhaciyef.common.model.payment.MappedPaymentModel
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.databinding.FragmentPaymentCategoriesBinding
import com.natiqhaciyef.prodocument.ui.util.NavigationManager
import com.natiqhaciyef.prodocument.ui.util.NavigationManager.HOME_ROUTE
import com.natiqhaciyef.prodocument.ui.util.BundleConstants
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.payment.adapter.PaymentMethodsAdapter
import com.natiqhaciyef.prodocument.ui.view.main.payment.contract.PaymentContract
import com.natiqhaciyef.prodocument.ui.view.main.payment.viewmodel.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass


@AndroidEntryPoint
class PaymentCategoriesFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPaymentCategoriesBinding = FragmentPaymentCategoriesBinding::inflate,
    override val viewModelClass: KClass<PaymentViewModel> = PaymentViewModel::class
) : BaseFragment<FragmentPaymentCategoriesBinding, PaymentViewModel, PaymentContract.PaymentState, PaymentContract.PaymentEvent, PaymentContract.PaymentEffect>() {
    private var adapter: PaymentMethodsAdapter? = null
    private var resourceBundle = bundleOf()
    private var paymentModel: MappedPaymentModel? = null
    private var pickedSubscriptionModel: MappedSubscriptionModel? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityConfig()
        val arg: PaymentCategoriesFragmentArgs by navArgs()
        resourceBundle = arg.datasetBundle
        pickedSubscriptionModel = resourceBundle.getParcelable(BundleConstants.BUNDLE_SUBSCRIPTION_PLAN)

        binding.addNewPaymentMethodButton.setOnClickListener { addNewPaymentMethodButtonClick() }
    }

    override fun onStateChange(state: PaymentContract.PaymentState) {
        when {
            state.isLoading -> {
                changeVisibilityOfProgressBar(true)
            }

            else -> {
                changeVisibilityOfProgressBar()

                if (state.paymentMethodsList != null)
                    recyclerViewConfig(state.paymentMethodsList!!)

                if (state.cheque != null){
                    resourceBundle.putParcelable(BundleConstants.BUNDLE_CHEQUE_PAYMENT, state.cheque!!)
                    resourceBundle.putParcelable(BundleConstants.BUNDLE_PAYMENT, paymentModel)
                    val action = PaymentCategoriesFragmentDirections.actionPaymentCategoriesFragmentToPaymentDetailsFragment(resourceBundle)
                    navigate(action)
                }
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
            with(it.binding) {
                bottomNavBar.visibility = View.GONE
                appbarLayout.visibility = View.VISIBLE

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

        viewModel.postEvent(PaymentContract.PaymentEvent.GetAllStoredPaymentMethods)
    }

    private fun onToolbarBackPressAction(bnw: BottomNavigationView){
        bnw.visibility = View.VISIBLE
        NavigationManager.navigateByRouteTitle(this, HOME_ROUTE)
    }

    private fun onScanIconClickAction() {
        val mappedSubscriptionModel = resourceBundle.getParcelable<MappedSubscriptionModel>(BundleConstants.BUNDLE_SUBSCRIPTION_PLAN)
        val subscriptionDetails = mappedSubscriptionModel?.toDetails()
        resourceBundle.putParcelable(BundleConstants.BUNDLE_SUBSCRIPTION_PAYMENT_DETAILS, subscriptionDetails)

        val action = PaymentCategoriesFragmentDirections.actionPaymentCategoriesFragmentToScanFragment(resourceBundle)
        navigate(action)
    }

    private fun recyclerViewConfig(list: List<MappedPaymentModel>) {
        adapter = PaymentMethodsAdapter(list = list.toMutableList())
        adapter?.onClickAction = {
            // navigate to details screen with loading screen
            paymentModel = it
            viewModel.postEvent(
                PaymentContract.PaymentEvent.GetPaymentData(
                    paymentModel = it,
                    pickedPlan = pickedSubscriptionModel?.token ?: ""
                )
            )
        }

        binding.pickPaymentRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.pickPaymentRecyclerView.adapter = adapter

    }

    private fun addNewPaymentMethodButtonClick(){
        val action = PaymentCategoriesFragmentDirections.actionPaymentCategoriesFragmentToNewPaymentFragment()
        navigate(action)
    }
}