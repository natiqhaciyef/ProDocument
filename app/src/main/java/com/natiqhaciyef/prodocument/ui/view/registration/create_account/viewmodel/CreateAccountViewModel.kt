package com.natiqhaciyef.prodocument.ui.view.registration.create_account.viewmodel

import android.media.effect.Effect
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.common.model.UIResult
import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.common.model.mapped.MappedUserModel
import com.natiqhaciyef.domain.usecase.user.remote.CreateUserRemoteUseCase
import com.natiqhaciyef.domain.usecase.user.local.InsertUserLocalUseCase
import com.natiqhaciyef.prodocument.ui.base.BaseUIState
import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import com.natiqhaciyef.prodocument.ui.view.registration.create_account.contract.CreateAccountContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    private val saveUserToLocalUseCase: InsertUserLocalUseCase,
    private val createUserRemoteUseCase: CreateUserRemoteUseCase,
) : BaseViewModel<CreateAccountContract.CreateAccountState, CreateAccountContract.CreateAccountEvent, CreateAccountContract.CreateAccountEffect>() {
    private val _localResultState =
        MutableLiveData(BaseUIState<Boolean>())
    val localResultState: LiveData<BaseUIState<Boolean>>
        get() = _localResultState

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
                && model.email != "null"
                && model.password.isNotEmpty()
                && model.password != "null"
            ) {
                onSuccess()
            } else {
                onFail(Exception(ErrorMessages.EMPTY_FIELD))
            }
        }
    }


    fun saveToDatabase(
        model: MappedUserModel?,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            model?.let {
                val uiResult = UIResult(id = "0", data = model, publishDate = getNow())
                saveUserToLocalUseCase.run(uiResult).collectLatest {
                    when (it.status) {
                        Status.SUCCESS -> {
                            _localResultState.value = _localResultState.value?.copy(
                                obj = it.data,
                                list = listOf(),
                                isLoading = false,
                                isSuccess = true,
                                message = it.message,
                                failReason = null
                            )
                            onSuccess()
                        }

                        Status.ERROR -> {
                            _localResultState.value = _localResultState.value?.copy(
                                obj = null,
                                list = listOf(),
                                isLoading = false,
                                isSuccess = false,
                                message = it.message,
                                failReason = it.exception
                            )
                        }

                        Status.LOADING -> {
                            _localResultState.value = _localResultState.value?.copy(
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

    private fun createAccountNetwork(
        model: MappedUserModel?
    ) {
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