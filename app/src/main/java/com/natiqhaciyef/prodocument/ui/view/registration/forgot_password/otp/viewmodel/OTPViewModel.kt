package com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.otp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.domain.usecase.user.remote.SendOtpRemoteUseCase
import com.natiqhaciyef.prodocument.ui.base.BaseUIState
import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import com.natiqhaciyef.prodocument.ui.base.State
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
        var count = 60
        while (count > 0) {
            delay(1000)
            emit(count)
            count -= 1
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