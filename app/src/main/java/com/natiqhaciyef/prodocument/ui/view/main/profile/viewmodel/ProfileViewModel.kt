package com.natiqhaciyef.prodocument.ui.view.main.profile.viewmodel

import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.prodocument.ui.view.main.profile.contract.ProfileContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(

): BaseViewModel<ProfileContract.ProfileState, ProfileContract.ProfileEvent, ProfileContract.ProfileEffect>() {

    override fun getInitialState(): ProfileContract.ProfileState = ProfileContract.ProfileState()
}