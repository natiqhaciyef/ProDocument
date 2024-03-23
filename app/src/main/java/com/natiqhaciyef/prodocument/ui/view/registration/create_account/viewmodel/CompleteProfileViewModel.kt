package com.natiqhaciyef.prodocument.ui.view.registration.create_account.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.common.model.mapped.MappedUserModel
import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import com.natiqhaciyef.prodocument.ui.util.DefaultImplModels
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// zxing

@HiltViewModel
class CompleteProfileViewModel @Inject constructor() : BaseViewModel() {
    private val _userState =
        MutableLiveData(DefaultImplModels.mappedUserModel)
    val userState: LiveData<MappedUserModel>
        get() = _userState


    fun collectDataFromCompleteProfileScreen(
        data: MappedUserModel,
        onSuccess: () -> Unit = { },
        onFail: (Exception?) -> Unit = {}
    ) {
        viewModelScope.launch {
            if (
                data.name.isNotEmpty()
                && data.phoneNumber.isNotEmpty()
                && data.imageUrl.isNotEmpty()
                && data.imageUrl != "null"
                && data.birthDate.isNotEmpty()
                && data.gender.isNotEmpty()
            ) {
                _userState.value?.let {
                    it.name = data.name
                    it.phoneNumber = data.phoneNumber
                    it.imageUrl = data.imageUrl
                    it.birthDate = data.birthDate
                    it.gender = data.gender
                }

                onSuccess()
            } else {
                onFail(Exception(ErrorMessages.EMPTY_FIELD))
            }
        }
    }

}
