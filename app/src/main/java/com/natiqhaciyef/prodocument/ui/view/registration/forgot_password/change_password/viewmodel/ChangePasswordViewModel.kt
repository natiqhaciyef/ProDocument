package com.natiqhaciyef.prodocument.ui.view.registration.forgot_password.change_password.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.constants.CHANGE_PASSWORD_SUCCESS
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.domain.usecase.user.UpdateUserPasswordByEmailRemoteUseCase
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
        when (event) {
            is ChangePasswordContract.ChangePasswordEvent.UpdatePasswordEvent -> updatePassword(
                ctx = event.ctx,
                email = event.email,
                password = event.password
            )
        }
    }

    private fun updatePassword(ctx: Context, email: String, password: String) {
        if (email.isNotEmpty()
            && email != ctx.getString(com.natiqhaciyef.common.R.string.null_)
            && password.isNotEmpty()
            && password != ctx.getString(com.natiqhaciyef.common.R.string.null_)
        ) {
            val map = mapOf(
                ctx.getString(com.natiqhaciyef.common.R.string.email_str) to email,
                ctx.getString(com.natiqhaciyef.common.R.string.password_str) to password
            )
            viewModelScope.launch {

                updateUserPasswordByEmailRemoteUseCase.operate(map)
                    .collectLatest { result ->

                        when (result.status) {
                            Status.SUCCESS -> {
                                if (result.data != null) {
                                    setBaseState(
                                        getCurrentBaseState().copy(
                                            isLoading = false,
                                            tokenModel = result.data
                                        )
                                    )

                                    postEffect(
                                        ChangePasswordContract.ChangePasswordEffect.ResultAlertDialog(
                                            icon = com.natiqhaciyef.common.R.drawable.success_result_type_icon,
                                            messageType = ResultType.SUCCESS.title,
                                            messageDescription = CHANGE_PASSWORD_SUCCESS
                                        )
                                    )
                                }
                            }

                            Status.ERROR -> {
                                postEffect(
                                    ChangePasswordContract.ChangePasswordEffect.ResultAlertDialog(
                                        icon = com.natiqhaciyef.common.R.drawable.fail_result_type_icon,
                                        messageType = ResultType.FAIL.title,
                                        messageDescription = "${SOMETHING_WENT_WRONG}..."
                                    )
                                )
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