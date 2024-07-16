package com.natiqhaciyef.prodocument.ui.view.main.premium.viewmodel

import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.domain.usecase.subscription.GetAllSubscriptionPlansUseCase
import com.natiqhaciyef.prodocument.ui.view.main.premium.contract.PremiumContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
    private val getAllSubscriptionPlansUseCase: GetAllSubscriptionPlansUseCase,
) : BaseViewModel<PremiumContract.PremiumState, PremiumContract.PremiumEvent, PremiumContract.PremiumEffect>() {

    override fun onEventUpdate(event: PremiumContract.PremiumEvent) {
        when (event) {
            is PremiumContract.PremiumEvent.GetAllSubscriptionPlans -> {
                getAllSubscriptionPlans()
            }

            is PremiumContract.PremiumEvent.Clear -> {
                clearState()
            }
        }
    }

    private fun getAllSubscriptionPlans() {
        viewModelScope.launch {
            getAllSubscriptionPlansUseCase.invoke().collectLatest { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        setBaseState(
                            getCurrentBaseState().copy(
                                isLoading = false,
                                subscriptions = result.data
                            )
                        )

                    }

                    Status.ERROR -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = false))
                    }

                    Status.LOADING -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = true))
                    }
                }
            }
        }
    }

    private fun clearState(){
        setBaseState(getCurrentBaseState().copy())
    }

    override fun getInitialState(): PremiumContract.PremiumState = PremiumContract.PremiumState()
}