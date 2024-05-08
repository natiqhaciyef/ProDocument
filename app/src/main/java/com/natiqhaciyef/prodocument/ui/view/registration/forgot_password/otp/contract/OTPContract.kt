package com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.otp.contract

import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState

object OTPContract {
    sealed class OTPEvent : UiEvent {
        data class SendOTP(var otp: String) : OTPEvent()
    }

    sealed class OTPEffect : UiEffect {
        data class FailEffect(var message: String? = null, var exception: Exception? = null) :
            OTPEffect()
    }

    data class OTPState(
        override var isLoading: Boolean = false,
        var result: CRUDModel? = null
    ) : UiState

}