package com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough.contract

import com.natiqhaciyef.common.model.mapped.MappedUserWithoutPasswordModel
import com.natiqhaciyef.core.base.ui.UiEffect
import com.natiqhaciyef.core.base.ui.UiEvent
import com.natiqhaciyef.core.base.ui.UiState

object OnBoardingContract {

    sealed class OnBoardingEvent : UiEvent {
        data class SkipButtonClickEvent(
            var user: MappedUserWithoutPasswordModel?,
            var onAction: (String) -> Unit
        ): OnBoardingEvent()

        data class OnboardingEvent(
            var user: MappedUserWithoutPasswordModel?,
            var onAction: (String) -> Unit
        ): OnBoardingEvent()

        data object GetUserByTokenEvent : OnBoardingEvent()
    }

    sealed class OnboardingEffect : UiEffect {

    }

    data class OnboardingState(
        override var isLoading: Boolean = false,
        var user: MappedUserWithoutPasswordModel? = null
    ) : UiState
}
