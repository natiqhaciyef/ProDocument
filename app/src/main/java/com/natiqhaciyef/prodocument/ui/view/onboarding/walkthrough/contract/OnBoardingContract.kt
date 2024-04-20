package com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough.contract

import com.natiqhaciyef.common.model.mapped.MappedUserWithoutPasswordModel
import com.natiqhaciyef.prodocument.ui.base.UiEffect
import com.natiqhaciyef.prodocument.ui.base.UiEvent
import com.natiqhaciyef.prodocument.ui.base.UiState

object OnBoardingContract {

    sealed class OnBoardingEvent : UiEvent {
        data class SkipButtonClickEvent(
            var onAction: (String) -> Unit
        ): OnBoardingEvent()

        data class OnboardingEvent(
            var onAction: (String) -> Unit
        ): OnBoardingEvent()

        data class GetUserByEmailEvent(var email: String) : OnBoardingEvent()
    }

    sealed class OnboardingEffect : UiEffect {

    }

    data class OnboardingState(
        override var isLoading: Boolean = false,
        var user: MappedUserWithoutPasswordModel? = null
    ) : UiState
}
