package com.natiqhaciyef.prodocument.ui.view.main.premium.viewmodel

import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.domain.usecase.subscription.ActivatePickedPlanUseCase
import com.natiqhaciyef.domain.usecase.subscription.GetAllSubscriptionPlansUseCase
import com.natiqhaciyef.prodocument.ui.view.main.premium.contract.PremiumContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
    private val getAllSubscriptionPlansUseCase: GetAllSubscriptionPlansUseCase,
    private val activatePickedPlanUseCase: ActivatePickedPlanUseCase
) : BaseViewModel<PremiumContract.PremiumState, PremiumContract.PremiumEvent, PremiumContract.PremiumEffect>() {

    override fun onEventUpdate(event: PremiumContract.PremiumEvent) {
        when (event) {
            is PremiumContract.PremiumEvent.PickPlanEvent -> {
                pickingPlanForSubscription(event.planToken)
            }

            is PremiumContract.PremiumEvent.GetAllSubscriptionPlans -> {
                getAllSubscriptionPlans()
            }
        }
    }

    private fun pickingPlanForSubscription(
        planToken: String
    ) {
        viewModelScope.launch {
            activatePickedPlanUseCase.operate(planToken).collectLatest { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        setBaseState(
                            getCurrentBaseState().copy(
                                isLoading = false,
                                isPicked = result.data
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

    override fun getInitialState(): PremiumContract.PremiumState = PremiumContract.PremiumState()
}