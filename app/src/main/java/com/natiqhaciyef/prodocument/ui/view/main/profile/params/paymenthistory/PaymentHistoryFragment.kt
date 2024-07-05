package com.natiqhaciyef.prodocument.ui.view.main.profile.params.paymenthistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.natiqhaciyef.common.helpers.secondWordFirstLetterLowercase
import com.natiqhaciyef.common.model.payment.PaymentHistoryModel
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.core.base.ui.BaseRecyclerHolderStatefulFragment
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentPaymentHistoryBinding
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.profile.contract.ProfileContract
import com.natiqhaciyef.uikit.adapter.PaymentHistoryAdapter
import com.natiqhaciyef.prodocument.ui.view.main.profile.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class PaymentHistoryFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPaymentHistoryBinding = FragmentPaymentHistoryBinding::inflate,
    override val viewModelClass: KClass<ProfileViewModel> = ProfileViewModel::class
) : BaseRecyclerHolderStatefulFragment<
        FragmentPaymentHistoryBinding, ProfileViewModel, PaymentHistoryModel, PaymentHistoryAdapter,
        ProfileContract.ProfileState, ProfileContract.ProfileEvent, ProfileContract.ProfileEffect>() {
    override var adapter: PaymentHistoryAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.postEvent(ProfileContract.ProfileEvent.GetPaymentHistory)
        activityConfig()
    }

    override fun onStateChange(state: ProfileContract.ProfileState) {
        when {
            state.isLoading -> {
                errorResultConfig()
                changeVisibilityOfProgressBar(true)
            }

            state.paymentsHistory.isNullOrEmpty() -> {
                errorResultConfig(true)
                changeVisibilityOfProgressBar()
            }

            else -> {
                errorResultConfig()
                changeVisibilityOfProgressBar()
                if (state.paymentsHistory != null) {
                    recyclerViewConfig(state.paymentsHistory!!)
                }
            }
        }
    }

    override fun onEffectUpdate(effect: ProfileContract.ProfileEffect) {

    }

    private fun changeVisibilityOfProgressBar(isVisible: Boolean = false) {
        if (isVisible) {
            binding.apply {
                progressBar.visibility = View.VISIBLE
                progressBar.isIndeterminate = true
            }
        } else {
            binding.apply {
                progressBar.visibility = View.GONE
                progressBar.isIndeterminate = false
            }
        }
    }

    private fun errorResultConfig(isVisible: Boolean = false) {
        if (isVisible) {
            val title = getString(com.natiqhaciyef.common.R.string.payment_history)
                .secondWordFirstLetterLowercase()
            val resultText = getString(com.natiqhaciyef.common.R.string.empty_list_result, title)

            binding.errorTitle.text = resultText
            binding.errorTitle.visibility = View.VISIBLE
        } else {
            binding.errorTitle.visibility = View.GONE
        }
    }

    private fun activityConfig() {
        (activity as MainActivity).also {
            with(it.binding) {
                bottomNavBar.visibility = View.GONE
                materialToolbar.apply {
                    navigationIcon = null
                    visibility = View.VISIBLE
                    setTitleToolbar(getString(com.natiqhaciyef.common.R.string.payment_history))
                    changeVisibility(View.VISIBLE)
                    changeAppIcon(com.natiqhaciyef.common.R.drawable.back_arrow_icon) {
                        onToolbarBackPressAction(
                            bottomNavBar
                        )
                    }
                    appIconVisibility(View.VISIBLE)
                    setVisibilitySearch(View.GONE)
                    setVisibilityOptionsMenu(View.VISIBLE)
                    setIconToOptions(com.natiqhaciyef.common.R.drawable.options_icon)
                    setVisibilityToolbar(View.VISIBLE)
                }
            }
        }
    }

    private fun onToolbarBackPressAction(bnw: BottomNavigationView) {
        bnw.visibility = View.VISIBLE
        navigate(R.id.profileFragment)
    }

    override fun recyclerViewConfig(list: List<PaymentHistoryModel>) {
        adapter = PaymentHistoryAdapter(requireContext(), list.toMutableList())
        with(binding) {
            recyclerPaymentHistoryView.adapter = adapter
            recyclerPaymentHistoryView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }
}