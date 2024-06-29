package com.natiqhaciyef.prodocument.ui.view.registration.login.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.domain.usecase.user.SignInRemoteUseCase
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
        when (event) {
            is LoginContract.LoginEvent.LoginClickEvent -> signIn(
                event.ctx,
                event.email,
                event.password
            )

            is LoginContract.LoginEvent.GoogleSignInClickEvent -> signInWithGoogle()
            is LoginContract.LoginEvent.AppleSignInClickEvent -> signInWithApple()
            is LoginContract.LoginEvent.FacebookSignInClickEvent -> signInWithFacebook()
        }
    }

    private fun signInWithGoogle() {}

    private fun signInWithApple() {}

    private fun signInWithFacebook() {}

    private fun signIn(
        ctx: Context,
        email: String,
        password: String
    ) {
        val map = mapOf(
            ctx.getString(com.natiqhaciyef.common.R.string.email_str) to email,
            ctx.getString(com.natiqhaciyef.common.R.string.password_str) to password
        )
        viewModelScope.launch {
            if (email != ctx.getString(com.natiqhaciyef.common.R.string.null_)
                && email.isNotEmpty()
                && password != ctx.getString(com.natiqhaciyef.common.R.string.null_)
                && password.isNotEmpty()
            ) {
                signInRemoteUseCase.operate(map).collectLatest { result ->
                    when (result.status) {
                        Status.SUCCESS -> {
                            if (result.data != null) {
                                setBaseState(
                                    getCurrentBaseState().copy(
                                        isLoading = false,
                                        tokenModel = result.data
                                    )
                                )
                            }
                        }

                        Status.ERROR -> {
                            postEffect(
                                LoginContract.LoginEffect.LoginFailedEffect(
                                    message = result.message,
                                    exception = result.exception
                                )
                            )
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