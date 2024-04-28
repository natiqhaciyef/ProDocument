package com.natiqhaciyef.prodocument.ui.view.main.home.options.watermark.viewmodel

import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import com.natiqhaciyef.prodocument.ui.view.main.home.options.watermark.contract.WatermarkContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WatermarkViewModel @Inject constructor(

) : BaseViewModel<WatermarkContract.WatermarkState, WatermarkContract.WatermarkEvent, WatermarkContract.WatermarkEffect>() {


    override fun getInitialState(): WatermarkContract.WatermarkState = WatermarkContract.WatermarkState()
}