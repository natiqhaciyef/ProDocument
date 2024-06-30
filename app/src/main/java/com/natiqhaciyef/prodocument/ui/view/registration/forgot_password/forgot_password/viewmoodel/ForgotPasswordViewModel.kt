package com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.forgot_password.viewmoodel

import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.domain.usecase.user.GetOtpRemoteUseCase
import com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.forgot_password.contract.ForgotPasswordContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val getOtpRemoteUseCase: GetOtpRemoteUseCase
) : BaseViewModel<ForgotPasswordContract.ForgotPasswordState, ForgotPasswordContract.ForgotPasswordEvent, ForgotPasswordContract.ForgotPasswordEffect>() {
    override fun onEventUpdate(event: ForgotPasswordContract.ForgotPasswordEvent) {
        when(event){
            is ForgotPasswordContract.ForgotPasswordEvent.GetOtpEvent -> getOtpResult(event.email)
        }
    }

    private fun getOtpResult(email: String) {
        viewModelScope.launch {
            getOtpRemoteUseCase.operate(email).collectLatest { result ->
                when (result.status) {
                    Status.LOADING -> {
                        setBaseState(getCurrentBaseState().copy(
                            isLoading = true
                        ))
                    }

                    Status.SUCCESS -> {
                        if (result.data != null) {
                            setBaseState(getCurrentBaseState().copy(
                                isLoading = false,
                                result = result.data
                            ))
                        }
                    }

                    Status.ERROR -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = false))

                        postEffect(ForgotPasswordContract.ForgotPasswordEffect.FailEffect(
                            message = result.message,
                            exception = result.exception
                        ))
                    }
                }
            }
        }
    }

    override fun getInitialState(): ForgotPasswordContract.ForgotPasswordState = ForgotPasswordContract.ForgotPasswordState()
}