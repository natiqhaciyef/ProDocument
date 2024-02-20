package com.natiqhaciyef.prodocument.ui.view.registration.forgot_password

import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class OTPViewModel @Inject constructor(

) : BaseViewModel() {
    val timingFlow = flow {
        var count = 60
        while (count > 0) {
            delay(1000)
            emit(count)
            count -= 1
        }
    }
}