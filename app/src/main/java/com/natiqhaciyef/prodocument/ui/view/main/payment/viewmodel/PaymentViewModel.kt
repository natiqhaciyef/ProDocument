package com.natiqhaciyef.prodocument.ui.view.main.payment.viewmodel

import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.helpers.toPickedModelList
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.model.payment.MappedPaymentModel
import com.natiqhaciyef.common.model.payment.MappedPaymentPickModel
import com.natiqhaciyef.common.model.payment.PaymentDetails
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.domain.usecase.payment.local.GetStoredPaymentMethodsUseCase
import com.natiqhaciyef.domain.usecase.payment.local.RemovePaymentMethodUseCase
import com.natiqhaciyef.domain.usecase.payment.remote.GetAllSavedPaymentMethodsUseCase
import com.natiqhaciyef.domain.usecase.payment.remote.GetChequePdfUseCase
import com.natiqhaciyef.domain.usecase.payment.remote.GetPickedPaymentDetailsUseCase
import com.natiqhaciyef.domain.usecase.payment.remote.StartPaymentUseCase
import com.natiqhaciyef.prodocument.ui.view.main.payment.contract.PaymentContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val getAllSavedPaymentMethodsUseCase: GetAllSavedPaymentMethodsUseCase,
    private val pickPaymentMethodsUseCase: GetPickedPaymentDetailsUseCase,
    private val insertNewPaymentMethodRemoteUseCase: com.natiqhaciyef.domain.usecase.payment.remote.InsertNewPaymentMethodUseCase,
    private val startPaymentUseCase: StartPaymentUseCase,
    private val getChequePdfUseCase: GetChequePdfUseCase,
    private val getAllStoredPaymentMethodsUseCase: GetStoredPaymentMethodsUseCase,
    private val insertNewPaymentMethodLocaleUseCase: com.natiqhaciyef.domain.usecase.payment.local.InsertNewPaymentMethodUseCase,
    private val removePaymentMethodUseCase: RemovePaymentMethodUseCase
) : BaseViewModel<PaymentContract.PaymentState, PaymentContract.PaymentEvent, PaymentContract.PaymentEffect>() {

    override fun onEventUpdate(event: PaymentContract.PaymentEvent) {
        when (event) {
            is PaymentContract.PaymentEvent.PickPaymentMethod -> {
                pickPayment(event.pickedPaymentMethod)
            }

            is PaymentContract.PaymentEvent.AddNewPaymentMethod -> {
                insertNewPaymentMethod(event.paymentModel)
            }

            is PaymentContract.PaymentEvent.PayForPlan -> {
                payForPlan(event.paymentModel, event.isSaved)
            }

            is PaymentContract.PaymentEvent.GetAllStoredPaymentMethods -> {
                getAllStoredMethods()
            }
        }
    }

    private fun getAllStoredMethods() {
        viewModelScope.launch {
            getAllSavedPaymentMethodsUseCase.invoke().collectLatest { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        if (result.data != null)
                            setBaseState(getCurrentBaseState().copy(isLoading = false, paymentMethodsList = result.data))
                    }

                    Status.ERROR -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = false))
                    }

                    Status.LOADING -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = true))
                    }
                }
            }
        }
    }

    private fun pickPayment(mappedPaymentPickModel: MappedPaymentPickModel) {
        viewModelScope.launch {
            pickPaymentMethodsUseCase.operate(mappedPaymentPickModel).collectLatest { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        if (result.data != null)
                            setBaseState(getCurrentBaseState().copy(isLoading = false, mappedPaymentModel = result.data))
                    }

                    Status.ERROR -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = false))
                    }

                    Status.LOADING -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = true))
                    }
                }
            }
        }
    }

    private fun insertNewPaymentMethod(mappedPaymentModel: MappedPaymentModel) {
        viewModelScope.launch {
            insertNewPaymentMethodRemoteUseCase.operate(mappedPaymentModel).collectLatest { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        if (result.data != null)
                            setBaseState(getCurrentBaseState().copy(isLoading = false, paymentResult = result.data))
                    }

                    Status.ERROR -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = false))
                    }

                    Status.LOADING -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = true))
                    }
                }
            }
        }
    }

    private fun payForPlan(mappedPaymentModel: MappedPaymentModel, isSaved: Boolean) {
        viewModelScope.launch {
            startPaymentUseCase.operate(mappedPaymentModel).collectLatest { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        if (result.data != null)
                            setBaseState(getCurrentBaseState().copy(isLoading = false, cheque = result.data))
                    }

                    Status.ERROR -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = false))
                    }

                    Status.LOADING -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = true))
                    }
                }
            }
        }
    }

    private fun getAllStoredPaymentMethodsLocal(){
        viewModelScope.launch {
            getAllStoredPaymentMethodsUseCase.invoke().collectLatest { result ->
                when(result.status){
                    Status.SUCCESS -> {
                        if (result.data != null)
                            setBaseState(
                                getCurrentBaseState().copy(
                                    isLoading = false,
                                    paymentMethodsList = result.data!!.toPickedModelList()
                                )
                            )
                    }

                    Status.ERROR -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = false))
                    }

                    Status.LOADING -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = true))
                    }
                }
            }
        }
    }

    private fun insertNewPaymentMethodLocal(paymentModel: MappedPaymentModel){
        viewModelScope.launch {
            insertNewPaymentMethodLocaleUseCase.run(paymentModel).collectLatest {}
        }
    }

    private fun removePaymentMethodLocal(paymentModel: MappedPaymentModel){
        viewModelScope.launch {
            removePaymentMethodUseCase.run(paymentModel)
        }
    }

    override fun getInitialState(): PaymentContract.PaymentState =
        PaymentContract.PaymentState()
}