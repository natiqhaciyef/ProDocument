package com.natiqhaciyef.prodocument.ui.view.main.profile.params.paymenthistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.natiqhaciyef.common.model.payment.PaymentHistoryModel
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentPaymentHistoryBinding
import com.natiqhaciyef.prodocument.ui.view.main.MainActivity
import com.natiqhaciyef.prodocument.ui.view.main.profile.contract.ProfileContract
import com.natiqhaciyef.prodocument.ui.view.main.profile.params.paymenthistory.adapter.PaymentHistoryAdapter
import com.natiqhaciyef.prodocument.ui.view.main.profile.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class PaymentHistoryFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPaymentHistoryBinding = FragmentPaymentHistoryBinding::inflate,
    override val viewModelClass: KClass<ProfileViewModel> = ProfileViewModel::class
) : BaseFragment<FragmentPaymentHistoryBinding, ProfileViewModel, ProfileContract.ProfileState, ProfileContract.ProfileEvent, ProfileContract.ProfileEffect>() {
    private var paymentAdapter: PaymentHistoryAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.postEvent(ProfileContract.ProfileEvent.GetPaymentHistory)
        activityConfig()
    }

    override fun onStateChange(state: ProfileContract.ProfileState) {
        when {
            state.isLoading -> {

            }

            else -> {
                if (state.paymentsHistory != null){
                    recyclerConfig(state.paymentsHistory!!.toMutableList())
                }
            }
        }
    }

    override fun onEffectUpdate(effect: ProfileContract.ProfileEffect) {

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

    private fun recyclerConfig(list: MutableList<PaymentHistoryModel>) {
        paymentAdapter = PaymentHistoryAdapter(requireContext(), list)
        with(binding) {
            recyclerPaymentHistoryView.adapter = paymentAdapter
            recyclerPaymentHistoryView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }
}