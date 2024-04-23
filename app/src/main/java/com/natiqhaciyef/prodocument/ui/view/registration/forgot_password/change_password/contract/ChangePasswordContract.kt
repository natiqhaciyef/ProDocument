package com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.change_password.contract

import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.prodocument.ui.base.UiEffect
import com.natiqhaciyef.prodocument.ui.base.UiEvent
import com.natiqhaciyef.prodocument.ui.base.UiState

object ChangePasswordContract {
    sealed class ChangePasswordEvent: UiEvent {
        data class UpdatePasswordEvent(val email: String, val password: String): ChangePasswordEvent()
    }

    sealed class ChangePasswordEffect: UiEffect{
        data class ResultAlertDialog(
            var messageType: String,
            var messageDescription: String,
            var icon: Int
        ): ChangePasswordEffect()
    }

    data class ChangePasswordState(
        override var isLoading: Boolean = false,
        var tokenModel: MappedTokenModel? = null
    ): UiState
}