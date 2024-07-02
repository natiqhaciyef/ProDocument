package com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough.viewmodel

import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.model.mapped.MappedUserWithoutPasswordModel
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.domain.usecase.user.GetUserByTokenRemoteUseCase
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil.HOME_ROUTE
import com.natiqhaciyef.prodocument.ui.util.NavigationUtil.REGISTER_ROUTE
import com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough.contract.OnBoardingContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val getUserByTokenRemoteUseCase: GetUserByTokenRemoteUseCase
) : BaseViewModel<OnBoardingContract.OnboardingState, OnBoardingContract.OnBoardingEvent, OnBoardingContract.OnboardingEffect>() {

    override fun onEventUpdate(event: OnBoardingContract.OnBoardingEvent) {
        when (event) {
            is OnBoardingContract.OnBoardingEvent.GetUserByTokenEvent -> {
                getUserByToken()
            }

            is OnBoardingContract.OnBoardingEvent.SkipButtonClickEvent -> {
                actionForOnBoarding(event.user, event.onAction)
            }

            is OnBoardingContract.OnBoardingEvent.OnboardingEvent -> {
                onboardingAction(event.user, event.onAction)
            }
        }
    }

    private fun onboardingAction(
        user: MappedUserWithoutPasswordModel?,
        action: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            delay(3500)
            actionForOnBoarding(user,action)
        }
    }

    private fun actionForOnBoarding(
        user: MappedUserWithoutPasswordModel?,
        action: (String) -> Unit = {}
    ) {
        if (user != null) {
            action(HOME_ROUTE)
        } else {
//            action(HOME_ROUTE)
            action(REGISTER_ROUTE)
        }
    }

    private fun getUserByToken() {
        viewModelScope.launch {
            getUserByTokenRemoteUseCase.invoke().collectLatest { result ->
                when (result.status) {
                    Status.LOADING -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = true))
                    }

                    Status.SUCCESS -> {
                        setBaseState(
                            getCurrentBaseState().copy(
                                isLoading = false,
                                user = result.data
                            )
                        )
                    }

                    Status.ERROR -> {
                            setBaseState(getCurrentBaseState().copy(isLoading = false))
                    }
                }
            }
        }
    }

    override fun getInitialState(): OnBoardingContract.OnboardingState =
        OnBoardingContract.OnboardingState()
}