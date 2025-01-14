package com.natiqhaciyef.prodocument.ui.view.registration.create_account.viewmodel

import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.constants.EMPTY_FIELD
import com.natiqhaciyef.common.constants.NULL
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.model.mapped.MappedUserModel
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.domain.usecase.user.CreateUserRemoteUseCase
import com.natiqhaciyef.prodocument.ui.view.registration.create_account.contract.CreateAccountContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    private val createUserRemoteUseCase: CreateUserRemoteUseCase,
) : BaseViewModel<CreateAccountContract.CreateAccountState, CreateAccountContract.CreateAccountEvent, CreateAccountContract.CreateAccountEffect>() {

    override fun onEventUpdate(event: CreateAccountContract.CreateAccountEvent) {
        when (event) {
            is CreateAccountContract.CreateAccountEvent.FinishButtonClickEvent -> {
                createUser(event.user)
            }
        }
    }

    private fun collectDataFromCreateAccountScreen(
        model: MappedUserModel,
        onSuccess: () -> Unit = {},
        onFail: (Exception?) -> Unit = {}
    ) {
        viewModelScope.launch {
            if (
                model.email.isNotEmpty()
                && model.email != NULL
                && model.password.isNotEmpty()
                && model.password != NULL
            ) {
                onSuccess()
            } else {
                onFail(Exception(EMPTY_FIELD))
            }
        }
    }

    private fun createAccountNetwork(model: MappedUserModel?) {
        viewModelScope.launch {
            model?.let {
                createUserRemoteUseCase.operate(model).collectLatest { result ->
                    when (result.status) {
                        Status.SUCCESS -> {
                            setBaseState(
                                getCurrentBaseState().copy(
                                    isLoading = false,
                                    token = result.data
                                )
                            )
                            postEffect(CreateAccountContract.CreateAccountEffect.UserCreationSucceedEffect)
                        }

                        Status.ERROR -> {
                            setBaseState(getCurrentBaseState().copy(isLoading = false))
                            postEffect(
                                CreateAccountContract.CreateAccountEffect.UserCreationFailedEffect(
                                    message = result.message,
                                    error = result.exception
                                )
                            )
                        }

                        Status.LOADING -> {
                            setBaseState(getCurrentBaseState().copy(isLoading = true))
                        }
                    }
                }
            }
        }
    }

    private fun createUser(mappedUserModel: MappedUserModel?) {
        viewModelScope.launch {
            mappedUserModel?.let {
                collectDataFromCreateAccountScreen(
                    model = mappedUserModel,
                    onSuccess = {
                        createAccountNetwork(mappedUserModel)
                    },
                    onFail = { exception ->
                        postEffect(
                            CreateAccountContract.CreateAccountEffect.UserCreationFailedEffect(
                                message = exception?.message,
                                error = exception
                            )
                        )
                    }
                )
            }
        }
    }

    override fun getInitialState(): CreateAccountContract.CreateAccountState =
        CreateAccountContract.CreateAccountState()

}