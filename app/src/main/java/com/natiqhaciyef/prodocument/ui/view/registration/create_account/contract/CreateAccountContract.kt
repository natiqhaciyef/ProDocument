package com.natiqhaciyef.prodocument.ui.view.registration.create_account.contract

import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.common.model.mapped.MappedUserModel
import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState

object CreateAccountContract {

    sealed class CreateAccountEvent : UiEvent {
        data class FinishButtonClickEvent(val user: MappedUserModel): CreateAccountEvent()

    }

    sealed class CreateAccountEffect: UiEffect {
        data class FieldNotCorrectlyFilledEffect(
            var message: String? = null,
            var error: Exception? = null
        ) : CreateAccountEffect()

        data class UserCreationFailedEffect(
            var message: String? = null,
            var error: Exception? = null
        ): CreateAccountEffect()

        data object UserCreationSucceedEffect : CreateAccountEffect()
    }

    data class CreateAccountState(
        override var isLoading: Boolean = false,
        var token: MappedTokenModel? = null,
    ): UiState
}