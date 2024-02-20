package com.natiqhaciyef.prodocument.ui.view.registration.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.prodocument.common.clazz.Status
import com.natiqhaciyef.prodocument.common.objects.ErrorMessages
import com.natiqhaciyef.prodocument.data.network.response.TokenResponse
import com.natiqhaciyef.prodocument.domain.usecase.user.SignInRemoteUseCase
import com.natiqhaciyef.prodocument.ui.base.BaseUIState
import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInRemoteUseCase: SignInRemoteUseCase
) : BaseViewModel() {
    private val _tokenState = MutableLiveData<BaseUIState<TokenResponse>>(BaseUIState())
    val tokenState: LiveData<BaseUIState<TokenResponse>>
        get() = _tokenState


    fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit = {},
        onFail: (Exception?) -> Unit = {}
    ) {
        val map = mapOf("email" to email, "password" to password)
        viewModelScope.launch {
            if (email != "null"
                && email.isNotEmpty()
                && password != "null"
                && password.isNotEmpty()
            ) {
                signInRemoteUseCase.operate(map).collectLatest { result ->
                    when (result.status) {
                        Status.SUCCESS -> {
                            if (result.data != null) {
                                _tokenState.value?.apply {
                                    obj = result.data
                                    list = listOf()
                                    isLoading = false
                                    isSuccess = true
                                    message = null
                                    failReason = null
                                }
                                onSuccess()
                            }
                        }

                        Status.ERROR -> {
                            _tokenState.value?.apply {
                                obj = null
                                list = listOf()
                                isLoading = false
                                isSuccess = false
                                message = result.message
                                failReason = result.exception
                            }
                        }

                        Status.LOADING -> {
                            _tokenState.value?.apply {
                                obj = null
                                list = listOf()
                                isLoading = true
                                isSuccess = false
                                message = null
                                failReason = null
                            }
                        }
                    }
                }
            } else {
                onFail(Exception(ErrorMessages.EMPTY_FIELD))
            }
        }
    }
}