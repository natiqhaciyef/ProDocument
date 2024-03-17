package com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.domain.usecase.user.GetOtpRemoteUseCase
import com.natiqhaciyef.prodocument.ui.base.BaseUIState
import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val getOtpRemoteUseCase: GetOtpRemoteUseCase
) : BaseViewModel() {
    private val _otpResultState = MutableLiveData(BaseUIState<CRUDModel>())
    val otpResultState: LiveData<BaseUIState<CRUDModel>>
        get() = _otpResultState

    fun getOtpResult(
        email: String,
    ) {
        viewModelScope.launch {
            getOtpRemoteUseCase.operate(email).collectLatest { result ->
                when (result.status) {
                    Status.LOADING -> {
                        _otpResultState.value?.apply {
                            obj = null
                            list = listOf()
                            isLoading = true
                            isSuccess = false
                            message = null
                            failReason = null
                        }
                    }

                    Status.SUCCESS -> {
                        if (result.data != null) {
                            _otpResultState.value?.apply {
                                obj = result.data
                                list = listOf()
                                isLoading = false
                                isSuccess = true
                                message = null
                                failReason = null
                            }
                        }
                    }

                    Status.ERROR -> {
                        _otpResultState.value?.apply {
                            obj = null
                            list = listOf()
                            isLoading = false
                            isSuccess = false
                            message = result.message
                            failReason = result.exception
                        }
                    }
                }
            }
        }
    }
}