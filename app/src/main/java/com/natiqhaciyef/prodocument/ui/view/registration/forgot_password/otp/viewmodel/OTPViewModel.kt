package com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.otp.viewmodel

import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.constants.SIXTY
import com.natiqhaciyef.common.constants.THOUSAND
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.domain.usecase.user.SendOtpRemoteUseCase
import com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.otp.contract.OTPContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OTPViewModel @Inject constructor(
    private val sendOtpRemoteUseCase: SendOtpRemoteUseCase
) : BaseViewModel<OTPContract.OTPState, OTPContract.OTPEvent, OTPContract.OTPEffect>() {

    val timingFlow = flow {
        var count = SIXTY
        while (count > ZERO) {
            delay(THOUSAND.toLong())
            emit(count)
            count -= ONE
        }
    }

    override fun onEventUpdate(event: OTPContract.OTPEvent) {
        when(event){
            is OTPContract.OTPEvent.SendOTP -> sendOtp(event.otp)
        }
    }


    private fun sendOtp(otp: String){
        viewModelScope.launch {
            sendOtpRemoteUseCase.operate(otp).collectLatest {result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        if (result.data != null) {
                            setBaseState(getCurrentBaseState().copy(
                                isLoading = false,
                                result = result.data
                            ))
                        }
                    }

                    Status.ERROR -> {
                        postEffect(OTPContract.OTPEffect.FailEffect(
                            exception = result.exception,
                            message = result.message
                        ))

                        setBaseState(getCurrentBaseState().copy(isLoading = false))
                    }

                    Status.LOADING -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = true))
                    }
                }
            }
        }
    }

    override fun getInitialState(): OTPContract.OTPState = OTPContract.OTPState()
}