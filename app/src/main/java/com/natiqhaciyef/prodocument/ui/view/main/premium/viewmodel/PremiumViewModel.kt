package com.natiqhaciyef.prodocument.ui.view.main.premium.viewmodel

import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import com.natiqhaciyef.prodocument.ui.base.State
import com.natiqhaciyef.prodocument.ui.base.TotalUIState
import com.natiqhaciyef.prodocument.ui.view.main.premium.event.PremiumEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(

): BaseViewModel<PremiumEvent>() {
    override fun getInitialState(): State = State(TotalUIState.Empty)


}