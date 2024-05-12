package com.natiqhaciyef.prodocument.ui.view.main.home.options.protect.viewmodel

import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.prodocument.ui.view.main.home.options.protect.contract.ProtectFileContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProtectFileViewModel @Inject constructor(

) : BaseViewModel<ProtectFileContract.ProtectFileState, ProtectFileContract.ProtectFileEvent, ProtectFileContract.ProtectFileEffect>() {



    override fun getInitialState(): ProtectFileContract.ProtectFileState =
        ProtectFileContract.ProtectFileState()
}