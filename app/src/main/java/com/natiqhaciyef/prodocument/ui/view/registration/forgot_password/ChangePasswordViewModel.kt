package com.natiqhaciyef.prodocument.ui.view.registration.forgot_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.prodocument.common.clazz.Status
import com.natiqhaciyef.prodocument.data.network.response.CRUDResponse
import com.natiqhaciyef.prodocument.domain.usecase.user.UpdateUserPasswordByEmailRemoteUseCase
import com.natiqhaciyef.prodocument.ui.base.BaseUIState
import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val updateUserPasswordByEmailRemoteUseCase: UpdateUserPasswordByEmailRemoteUseCase
) : BaseViewModel() {
    private val _updateResultState = MutableLiveData<BaseUIState<CRUDResponse>>(BaseUIState())
    val updateResultState: LiveData<BaseUIState<CRUDResponse>>
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
                                    _updateResultState.value?.apply {
                                        obj = result.data
                                        list = listOf()
                                        isLoading = false
                                        isSuccess = true
                                        message = null
                                        failReason = null
                                    }
                                }
                            }

                            Status.ERROR -> {
                                _updateResultState.value?.apply {
                                    obj = null
                                    list = listOf()
                                    isLoading = false
                                    isSuccess = false
                                    message = result.message
                                    failReason = result.exception
                                }
                            }

                            Status.LOADING -> {
                                _updateResultState.value?.apply {
                                    obj = null
                                    list = listOf()
                                    isLoading = true
                                    isSuccess = false
                                    message = null
                                    failReason = null
                                }
                            }
                        }
                    }
            }
        }
    }
}