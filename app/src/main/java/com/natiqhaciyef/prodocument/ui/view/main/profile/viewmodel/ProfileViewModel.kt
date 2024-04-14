package com.natiqhaciyef.prodocument.ui.view.main.profile.viewmodel

import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import com.natiqhaciyef.prodocument.ui.base.State
import com.natiqhaciyef.prodocument.ui.base.TotalUIState
import com.natiqhaciyef.prodocument.ui.view.main.profile.event.ProfileEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(

): BaseViewModel<ProfileEvent>() {
    override fun getInitialState(): State = State(TotalUIState.Empty)
}