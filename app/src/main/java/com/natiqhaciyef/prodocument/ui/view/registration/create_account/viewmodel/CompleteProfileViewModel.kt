package com.natiqhaciyef.prodocument.ui.view.registration.create_account.viewmodel

import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.constants.EMPTY_FIELD
import com.natiqhaciyef.common.model.mapped.MappedUserModel
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.prodocument.ui.util.DefaultImplModels
import com.natiqhaciyef.prodocument.ui.view.registration.create_account.contract.CompleteProfileContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CompleteProfileViewModel @Inject constructor() :
    BaseViewModel<CompleteProfileContract.CompleteUiState, CompleteProfileContract.CompleteUiEvent, CompleteProfileContract.CompleteUiEffect>() {

    override fun onEventUpdate(event: CompleteProfileContract.CompleteUiEvent) {
        when (event) {
            is CompleteProfileContract.CompleteUiEvent.CollectUserData -> {
                collectDataFromCompleteProfileScreen(event.user)
            }
        }
    }

    private fun collectDataFromCompleteProfileScreen(data: MappedUserModel) {
        viewModelScope.launch {
            if (
                data.name.isNotEmpty()
                && data.phoneNumber.isNotEmpty()
                && data.imageUrl.isNotEmpty()
                && data.imageUrl != "null"
                && data.birthDate.isNotEmpty()
                && data.gender.isNotEmpty()
            ) {
                val mappedUserModel = DefaultImplModels.mappedUserModel.apply {
                    name = data.name
                    phoneNumber = data.phoneNumber
                    imageUrl = data.imageUrl
                    birthDate = data.birthDate
                    gender = data.gender
                }

                setBaseState(getCurrentBaseState().copy(user = mappedUserModel))
            } else {
                setBaseState(getCurrentBaseState().copy(isLoading = false))
                postEffect(
                    CompleteProfileContract.CompleteUiEffect.FieldNotCorrectlyFilledEffect(
                        error = Exception(EMPTY_FIELD),
                        message = EMPTY_FIELD
                    )
                )
            }
        }
    }

    override fun getInitialState(): CompleteProfileContract.CompleteUiState =
        CompleteProfileContract.CompleteUiState()
}
