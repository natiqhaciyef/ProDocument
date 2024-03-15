package com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.model.UIResult
import com.natiqhaciyef.common.model.mapped.MappedUserModel
import com.natiqhaciyef.domain.usecase.user.GetUserByTokenRemoteUseCase
import com.natiqhaciyef.prodocument.ui.base.BaseNavigationDeepLink.HOME_ROUTE
import com.natiqhaciyef.prodocument.ui.base.BaseUIState
import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val getUserByTokenRemoteUseCase: GetUserByTokenRemoteUseCase
) : BaseViewModel() {
    private val _userState = MutableLiveData(BaseUIState<UIResult<MappedUserModel>>())
    val userState: MutableLiveData<BaseUIState<UIResult<MappedUserModel>>>
        get() = _userState

    fun onboardingAction(
        onAction: (String) -> Unit = {},
    ) {
        viewModelScope.launch {
            delay(3500)
            actionForOnBoarding(onAction)
        }
    }

    fun actionForOnBoarding(onAction: (String) -> Unit) {
        if (userState.value != null && userState.value?.obj != null) {
            onAction(HOME_ROUTE)
        } else {
            onAction(HOME_ROUTE)
//            onAction(REGISTER_ROUTE)
        }
    }

    fun getUserByToken(token: String) {
        viewModelScope.launch {
            getUserByTokenRemoteUseCase.operate(token).collectLatest { result ->
                when (result.status) {
                    Status.LOADING -> {
                        _userState.value?.apply {
                            isLoading = true
                            obj = null
                            isSuccess = false
                            failReason = null
                            list = listOf()
                            message = null
                        }
                    }

                    Status.SUCCESS -> {
                        _userState.value?.apply {
                            isLoading = false
                            obj = result.data
                            isSuccess = true
                            failReason = null
                            list = listOf()
                            message = null
                        }
                    }

                    Status.ERROR -> {
                        _userState.value?.apply {
                            isLoading = false
                            obj = null
                            isSuccess = false
                            failReason = result.exception
                            list = listOf()
                            message = result.message
                        }
                    }
                }
            }
        }
    }
}