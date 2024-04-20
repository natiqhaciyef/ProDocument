package com.natiqhaciyef.prodocument.ui.view.registration.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.domain.usecase.user.remote.SignInRemoteUseCase
import com.natiqhaciyef.prodocument.ui.base.BaseUIState
import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import com.natiqhaciyef.prodocument.ui.base.State
import com.natiqhaciyef.prodocument.ui.view.registration.login.contract.LoginContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInRemoteUseCase: SignInRemoteUseCase
) : BaseViewModel<LoginContract.LoginState, LoginContract.LoginEvent, LoginContract.LoginEffect>() {

    override fun onEventUpdate(event: LoginContract.LoginEvent) {
        when(event) {
            is LoginContract.LoginEvent.LoginClickEvent -> signIn(event.email, event.password)
            is LoginContract.LoginEvent.GoogleSignInClickEvent -> signInWithGoogle()
            is LoginContract.LoginEvent.AppleSignInClickEvent -> signInWithApple()
            is LoginContract.LoginEvent.FacebookSignInClickEvent -> signInWithFacebook()
        }
    }

    private fun signInWithGoogle(){}

    private fun signInWithApple(){}

    private fun signInWithFacebook(){}

    private fun signIn(
        email: String,
        password: String
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
                                setBaseState(getCurrentBaseState().copy(isLoading = false, tokenModel = result.data))
                            }
                        }

                        Status.ERROR -> {
                            postEffect(LoginContract.LoginEffect.LoginFailedEffect(
                                message = result.message,
                                exception = result.exception
                            ))
                            setBaseState(getCurrentBaseState().copy(isLoading = false))
                        }

                        Status.LOADING -> {
                            setBaseState(getCurrentBaseState().copy(isLoading = true))
                        }
                    }
                }
            } else {
                postEffect(LoginContract.LoginEffect.EmptyFieldEffect)
//                Exception(ErrorMessages.EMPTY_FIELD)
            }
        }
    }

    override fun getInitialState(): LoginContract.LoginState = LoginContract.LoginState()
}