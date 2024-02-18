package com.natiqhaciyef.prodocument.ui.view.registration.create_account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.prodocument.common.clazz.Status
import com.natiqhaciyef.prodocument.common.helpers.getNow
import com.natiqhaciyef.prodocument.common.objects.ErrorMessages
import com.natiqhaciyef.prodocument.data.network.response.TokenResponse
import com.natiqhaciyef.prodocument.domain.model.UIResult
import com.natiqhaciyef.prodocument.domain.model.mapped.MappedUserModel
import com.natiqhaciyef.prodocument.domain.usecase.user.CreateUserRemoteUseCase
import com.natiqhaciyef.prodocument.domain.usecase.user.InsertUserLocalUseCase
import com.natiqhaciyef.prodocument.ui.base.BaseUIState
import com.natiqhaciyef.prodocument.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    private val saveUserToLocalUseCase: InsertUserLocalUseCase,
    private val createUserRemoteUseCase: CreateUserRemoteUseCase,
) : BaseViewModel() {
    private val _localResultState =
        MutableLiveData(BaseUIState<Boolean>())
    val localResultState: LiveData<BaseUIState<Boolean>>
        get() = _localResultState

    private val _tokenState =
        MutableLiveData(BaseUIState<TokenResponse>())
    val tokenState: LiveData<BaseUIState<TokenResponse>>
        get() = _tokenState

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
                val uiResult = UIResult(id = 0, data = model, publishDate = getNow())
                saveUserToLocalUseCase.run(uiResult).collectLatest {
                    when (it.status) {
                        Status.SUCCESS -> {
                            _localResultState.value?.apply {
                                obj = it.data
                                list = listOf()
                                isLoading = false
                                isSuccess = true
                                message = it.message
                                failReason = null
                            }
                            onSuccess()
                        }

                        Status.ERROR -> {
                            _localResultState.value?.apply {
                                obj = null
                                list = listOf()
                                isLoading = false
                                isSuccess = false
                                message = it.message
                                failReason = it.exception
                            }
                        }

                        Status.LOADING -> {
                            _localResultState.value?.apply {
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

    private fun createAccountNetwork(
        model: MappedUserModel?,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            model?.let {
                createUserRemoteUseCase.operate(model).collectLatest { result ->
                    when (result.status) {
                        Status.SUCCESS -> {
                            if (result.data != null) {
                                _tokenState.value?.apply {
                                    obj = result.data
                                    list = listOf()
                                    isLoading = false
                                    isSuccess = true
                                    message = null
                                    failReason = null
                                }

                                onSuccess()
                            }
                        }

                        Status.ERROR -> {
                            _tokenState.value?.apply {
                                obj = null
                                list = listOf()
                                isLoading = false
                                isSuccess = false
                                message = result.message
                                failReason = result.exception
                            }
                        }

                        Status.LOADING -> {
                            _tokenState.value?.apply {
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

    fun clickButtonAction(
        mappedUserModel: MappedUserModel?,
        onSuccess: () -> Unit = { },
        onFail: (Exception?) -> Unit = {}
    ) {
        viewModelScope.launch {
            mappedUserModel?.let {
                collectDataFromCreateAccountScreen(
                    model = mappedUserModel,
                    onSuccess = {
//                        createAccountNetwork(mappedUserModel, onSuccess)
                        onSuccess()
                    },
                    onFail = onFail
                )
            }
        }
    }

}