package com.natiqhaciyef.prodocument.ui.view.main.premium.viewmodel

import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import com.natiqhaciyef.prodocument.ui.view.main.premium.contract.PremiumContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(

): BaseViewModel<PremiumContract.PremiumState, PremiumContract.PremiumEvent, PremiumContract.PremiumEffect>() {
    override fun getInitialState(): PremiumContract.PremiumState = PremiumContract.PremiumState()


}