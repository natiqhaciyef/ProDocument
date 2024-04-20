package com.natiqhaciyef.prodocument.ui.view.registration.login.contract

import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.prodocument.ui.base.UiEffect
import com.natiqhaciyef.prodocument.ui.base.UiEvent
import com.natiqhaciyef.prodocument.ui.base.UiState
import java.lang.Exception

object LoginContract {
    sealed class LoginEvent : UiEvent {
        data class LoginClickEvent(val email: String, val password: String) : LoginEvent()
        data object GoogleSignInClickEvent : LoginEvent()
        data object AppleSignInClickEvent : LoginEvent()
        data object FacebookSignInClickEvent : LoginEvent()
    }

    sealed class LoginEffect : UiEffect {
        data class LoginFailedEffect(
            var message: String? = null,
            var exception: Exception? = null
        ): LoginEffect()

        data object EmptyFieldEffect: LoginEffect()
    }

    data class LoginState(
        override var isLoading: Boolean = false,
        var tokenModel: MappedTokenModel? = null
    ) : UiState
}