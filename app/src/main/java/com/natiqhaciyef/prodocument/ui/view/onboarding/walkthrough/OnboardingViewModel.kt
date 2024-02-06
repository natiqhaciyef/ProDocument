package com.natiqhaciyef.prodocument.ui.view.onboarding.walkthrough

import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.natiqhaciyef.prodocument.ui.base.BaseNavigationDeepLink.HOME_ROUTE
import com.natiqhaciyef.prodocument.ui.base.BaseNavigationDeepLink.REGISTER_ROUTE
import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(

) : BaseViewModel() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun onboardingAction(
        onAction: (String) -> Unit = {},
    ) {
        viewModelScope.launch {
            delay(3500)
            actionForOnBoarding(onAction)
        }
    }

    fun actionForOnBoarding(onAction: (String) -> Unit) {
        if (auth.currentUser != null) {
            onAction(HOME_ROUTE)
        } else {
            onAction(REGISTER_ROUTE)
        }
    }
}