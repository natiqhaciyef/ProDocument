package com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.change_password.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.domain.usecase.user.remote.UpdateUserPasswordByEmailRemoteUseCase
import com.natiqhaciyef.prodocument.ui.base.BaseUIState
import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.change_password.contract.ChangePasswordContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val updateUserPasswordByEmailRemoteUseCase: UpdateUserPasswordByEmailRemoteUseCase
) : BaseViewModel<ChangePasswordContract.ChangePasswordState, ChangePasswordContract.ChangePasswordEvent, ChangePasswordContract.ChangePasswordEffect>() {
    private val _updateResultState = MutableLiveData<BaseUIState<MappedTokenModel>>(BaseUIState())
    val updateResultState: LiveData<BaseUIState<MappedTokenModel>>
        get() = _updateResultState

    fun updatePassword(email: String, password: String) {
        if (email.isNotEmpty()
            && email != "null"
            && password.isNotEmpty()
            && password != "null"
        ) {
            val map = mapOf("email" to email, "password" to password)
            viewModelScope.launch {

                updateUserPasswordByEmailRemoteUseCase.operate(map)
                    .collectLatest { result ->

                        when (result.status) {
                            Status.SUCCESS -> {
                                if (result.data != null) {
                                    _updateResultState.value = _updateResultState.value?.copy(
                                        obj = result.data,
                                        list = listOf(),
                                        isLoading = false,
                                        isSuccess = true,
                                        message = null,
                                        failReason = null
                                    )
                                }
                            }

                            Status.ERROR -> {
                                _updateResultState.value = _updateResultState.value?.copy(
                                    obj = null,
                                    list = listOf(),
                                    isLoading = false,
                                    isSuccess = false,
                                    message = result.message,
                                    failReason = result.exception
                                )
                            }

                            Status.LOADING -> {
                                _updateResultState.value = _updateResultState.value?.copy(
                                    obj = null,
                                    list = listOf(),
                                    isLoading = true,
                                    isSuccess = false,
                                    message = null,
                                    failReason = null
                                )
                            }
                        }
                    }
            }
        }
    }

    override fun getInitialState(): ChangePasswordContract.ChangePasswordState =
        ChangePasswordContract.ChangePasswordState()
}