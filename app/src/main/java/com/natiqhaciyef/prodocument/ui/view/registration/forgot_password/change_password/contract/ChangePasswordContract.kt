package com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.change_password.contract

import com.natiqhaciyef.prodocument.ui.base.UiEffect
import com.natiqhaciyef.prodocument.ui.base.UiEvent
import com.natiqhaciyef.prodocument.ui.base.UiState

object ChangePasswordContract {
    sealed class ChangePasswordEvent: UiEvent {
        data class UpdatePasswordEvent(val email: String): ChangePasswordEvent()
    }

    sealed class ChangePasswordEffect: UiEffect{
        data class ResultAlertDialog(
            var title: String,
            var messageTitle: String,
            var messageDescription: String
        ): ChangePasswordEffect()
    }

    data class ChangePasswordState(
        override var isLoading: Boolean = false,
    ): UiState
}