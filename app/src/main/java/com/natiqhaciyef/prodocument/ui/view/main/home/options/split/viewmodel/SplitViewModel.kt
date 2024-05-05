package com.natiqhaciyef.prodocument.ui.view.main.home.options.split.viewmodel

import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import com.natiqhaciyef.prodocument.ui.view.main.home.options.split.contract.SplitContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplitViewModel @Inject constructor(

): BaseViewModel<SplitContract.SplitState, SplitContract.SplitEvent, SplitContract.SplitEffect>() {



    override fun getInitialState(): SplitContract.SplitState = SplitContract.SplitState()
}