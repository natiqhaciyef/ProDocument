package com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.domain.usecase.user.remote.SendOtpRemoteUseCase
import com.natiqhaciyef.prodocument.ui.base.BaseUIState
import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import com.natiqhaciyef.prodocument.ui.base.State
import com.natiqhaciyef.prodocument.ui.base.TotalUIState
import com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.event.OTPEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OTPViewModel @Inject constructor(
    private val sendOtpRemoteUseCase: SendOtpRemoteUseCase
) : BaseViewModel<OTPEvent>() {
    private val _otpResultState = MutableLiveData<BaseUIState<CRUDModel>>(BaseUIState())
    val otpResultState: LiveData<BaseUIState<CRUDModel>>
        get() = _otpResultState

    val timingFlow = flow {
        var count = 60
        while (count > 0) {
            delay(1000)
            emit(count)
            count -= 1
        }
    }


    fun sendOtp(token: String, otp: String){
        viewModelScope.launch {
            sendOtpRemoteUseCase.operate(otp).collectLatest {result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        if (result.data != null) {
                            _otpResultState.value = _otpResultState.value?.copy(
                                obj = result.data,
                                list = listOf(),
                                isLoading = false,
                                isSuccess = true,
                                message = null,
                                failReason = null
                            )
                        }
                    }

                    Status.ERROR -> {
                        _otpResultState.value = _otpResultState.value?.copy(
                            obj = null,
                            list = listOf(),
                            isLoading = false,
                            isSuccess = false,
                            message = result.message,
                            failReason = result.exception
                        )
                    }

                    Status.LOADING -> {
                        _otpResultState.value = _otpResultState.value?.copy(
                            obj = null,
                            list = listOf(),
                            isLoading = true,
                            isSuccess = false,
                            message = null,
                            failReason = null
                        )
                    }
                }
            }
        }
    }

    override fun getInitialState(): State = State(TotalUIState.Empty)
}