package com.natiqhaciyef.prodocument.ui.view.registration.create_account.contract

import com.natiqhaciyef.common.model.mapped.MappedUserModel
import com.natiqhaciyef.prodocument.ui.base.UiEffect
import com.natiqhaciyef.prodocument.ui.base.UiEvent
import com.natiqhaciyef.prodocument.ui.base.UiState

object CompleteProfileContract {

    sealed class CompleteUiEvent : UiEvent {
        class CollectUserData(val user: MappedUserModel) : CompleteUiEvent()
    }

    sealed class CompleteUiEffect : UiEffect {
        data class FieldNotCorrectlyFilledEffect(
            var message: String? = null,
            var error: Exception? = null
        ) : CompleteUiEffect()
    }

    data class CompleteUiState(
        var user: MappedUserModel? = null,
        override var isLoading: Boolean = false,
    ) : UiState
}