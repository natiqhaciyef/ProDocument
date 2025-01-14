package com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.forgot_password.contract

import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState

object ForgotPasswordContract {

    sealed class ForgotPasswordEvent : UiEvent {
        data class GetOtpEvent(var email: String) : ForgotPasswordEvent()
    }

    sealed class ForgotPasswordEffect : UiEffect {
        data class FailEffect(var message: String? = null, var exception: Exception? = null): ForgotPasswordEffect()
    }

    data class ForgotPasswordState(
        override var isLoading: Boolean = false,
        var result: CRUDModel? = null
    ) : UiState
}