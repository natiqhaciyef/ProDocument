package com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.model.UIResult
import com.natiqhaciyef.common.model.mapped.MappedUserWithoutPasswordModel
import com.natiqhaciyef.domain.usecase.user.remote.GetUserByTokenRemoteUseCase
import com.natiqhaciyef.prodocument.ui.base.BaseNavigationDeepLink.HOME_ROUTE
import com.natiqhaciyef.prodocument.ui.base.BaseUIState
import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
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
    private val _userState =
        MutableLiveData(BaseUIState<UIResult<MappedUserWithoutPasswordModel>>())
    val userState: MutableLiveData<BaseUIState<UIResult<MappedUserWithoutPasswordModel>>>
        get() = _userState

    override fun onEventUpdate(event: OnBoardingContract.OnBoardingEvent) {
        when(event){
            is OnBoardingContract.OnBoardingEvent.GetUserByEmailEvent -> {getUserByEmail(event.email)}
            is OnBoardingContract.OnBoardingEvent.SkipButtonClickEvent -> {actionForOnBoarding(event.onAction)}
            is OnBoardingContract.OnBoardingEvent.OnboardingEvent -> {onboardingAction(event.onAction)}
        }
    }

    private fun onboardingAction(
        onAction: (String) -> Unit = {},
    ) {
        viewModelScope.launch {
            delay(3500)
            actionForOnBoarding(onAction)
        }
    }

    private fun actionForOnBoarding(onAction: (String) -> Unit) {
        if (userState.value != null && userState.value?.obj != null) {
            onAction(HOME_ROUTE)
        } else {
            onAction(HOME_ROUTE)
//            onAction(REGISTER_ROUTE)
        }
    }

    private fun getUserByEmail(email: String = "") {
        viewModelScope.launch {
            getUserByTokenRemoteUseCase.operate(email).collectLatest { result ->
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
//                            setBaseState(getCurrentBaseState().copy(isLoading = false, user = result.data?.data))
                    }
                }
            }
        }
    }

    override fun getInitialState(): OnBoardingContract.OnboardingState =
        OnBoardingContract.OnboardingState()
}