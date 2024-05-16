package com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.change_password.viewmodel

import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.common.objects.SuccessMessages
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.domain.usecase.user.remote.UpdateUserPasswordByEmailRemoteUseCase
import com.natiqhaciyef.core.model.ResultType
import com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.change_password.contract.ChangePasswordContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val updateUserPasswordByEmailRemoteUseCase: UpdateUserPasswordByEmailRemoteUseCase
) : BaseViewModel<ChangePasswordContract.ChangePasswordState, ChangePasswordContract.ChangePasswordEvent, ChangePasswordContract.ChangePasswordEffect>() {

    override fun onEventUpdate(event: ChangePasswordContract.ChangePasswordEvent) {
        when(event){
            is ChangePasswordContract.ChangePasswordEvent.UpdatePasswordEvent -> updatePassword(email = event.email, password = event.password)
        }
    }

    private fun updatePassword(email: String, password: String) {
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
                                    setBaseState(getCurrentBaseState().copy(
                                        isLoading = false,
                                        tokenModel = result.data
                                    ))

                                    postEffect(ChangePasswordContract.ChangePasswordEffect.ResultAlertDialog(
                                        icon = com.natiqhaciyef.common.R.drawable.success_result_type_icon,
                                        messageType = ResultType.SUCCESS.title,
                                        messageDescription = SuccessMessages.CHANGE_PASSWORD_SUCCESS
                                    ))
                                }
                            }

                            Status.ERROR -> {
                                postEffect(ChangePasswordContract.ChangePasswordEffect.ResultAlertDialog(
                                    icon = com.natiqhaciyef.common.R.drawable.fail_result_type_icon,
                                    messageType = ResultType.FAIL.title,
                                    messageDescription = "${ErrorMessages.SOMETHING_WENT_WRONG}..."
                                ))
                                setBaseState(ChangePasswordContract.ChangePasswordState(isLoading = false))
                            }

                            Status.LOADING -> {
                                setBaseState(ChangePasswordContract.ChangePasswordState(isLoading = true))
                            }
                        }
                    }
            }
        }
    }

    override fun getInitialState(): ChangePasswordContract.ChangePasswordState =
        ChangePasswordContract.ChangePasswordState()
}