package com.natiqhaciyef.prodocument.ui.view.registration.login.contract

import android.content.Context
import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState
import com.natiqhaciyef.core.store.AppStorePref
import java.lang.Exception

object LoginContract {
    sealed class LoginEvent : UiEvent {
        data class LoginClickEvent(
            val dataStore: AppStorePref,
            val ctx: Context, val email: String, val password: String) : LoginEvent()
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