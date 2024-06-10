package com.natiqhaciyef.prodocument.ui.view.main.payment.viewmodel

import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.model.payment.MappedPaymentChequeModel
import com.natiqhaciyef.common.model.payment.MappedPaymentModel
import com.natiqhaciyef.common.model.payment.MappedPaymentPickModel
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.domain.usecase.PAYMENT_MODEL
import com.natiqhaciyef.domain.usecase.PICKED_SUBSCRIPTION_PLAN
import com.natiqhaciyef.domain.usecase.payment.local.GetStoredPaymentMethodsUseCase
import com.natiqhaciyef.domain.usecase.payment.local.RemovePaymentMethodUseCase
import com.natiqhaciyef.domain.usecase.payment.remote.GetAllSavedPaymentMethodsUseCase
import com.natiqhaciyef.domain.usecase.payment.remote.GetChequePdfUseCase
import com.natiqhaciyef.domain.usecase.payment.remote.GetPaymentDataUseCase
import com.natiqhaciyef.domain.usecase.payment.remote.GetPickedPaymentDetailsUseCase
import com.natiqhaciyef.domain.usecase.payment.remote.StartPaymentUseCase
import com.natiqhaciyef.prodocument.ui.view.main.payment.contract.PaymentContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val getAllSavedPaymentMethodsUseCase: GetAllSavedPaymentMethodsUseCase,
    private val pickPaymentMethodsUseCase: GetPickedPaymentDetailsUseCase,
    private val insertNewPaymentMethodRemoteUseCase: com.natiqhaciyef.domain.usecase.payment.remote.InsertNewPaymentMethodUseCase,
    private val startPaymentUseCase: StartPaymentUseCase,
    private val getPaymentDataUseCase: GetPaymentDataUseCase,
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

            is PaymentContract.PaymentEvent.GetPaymentData -> {
                getPaymentData(event.paymentModel, event.pickedPlan)
            }

            is PaymentContract.PaymentEvent.GetChequePdf -> {
                getChequePdf(event.chequeId)
            }

            is PaymentContract.PaymentEvent.GetAllStoredPaymentMethods -> {
                getAllStoredMethods()
            }

            is PaymentContract.PaymentEvent.PayForPlan -> {
                startPayment(event.cheque)
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

    private fun getPaymentData(mappedPaymentModel: MappedPaymentModel, pickedPlanId: String) {
        val map = mapOf(
            PAYMENT_MODEL to mappedPaymentModel,
            PICKED_SUBSCRIPTION_PLAN to pickedPlanId
        )

        viewModelScope.launch {
            getPaymentDataUseCase.operate(map).collectLatest { result ->
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

    private fun startPayment(chequeModel: MappedPaymentChequeModel){
        viewModelScope.launch {
            startPaymentUseCase.operate(chequeModel).onEach { result ->

                when(result.status){
                    Status.SUCCESS -> {
                        if (result.data != null)
                            setBaseState(getCurrentBaseState().copy(paymentResult = result.data, isLoading = false))
                    }

                    Status.ERROR -> {
                        if (result.data != null)
                            setBaseState(getCurrentBaseState().copy(isLoading = false, paymentResult = result.data))
                        else
                            setBaseState(getCurrentBaseState().copy(isLoading = false))
                    }

                    Status.LOADING -> {
                        setBaseState(getCurrentBaseState().copy(isLoading = true))
                    }
                }

            }.launchIn(viewModelScope)

        }
    }

    private fun getChequePdf(chequeId: String){
        viewModelScope.launch {
            getChequePdfUseCase.operate(chequeId).collectLatest { result ->
                when(result.status){
                    Status.SUCCESS -> {
                        if (result.data != null)
                            setBaseState(getCurrentBaseState().copy(isLoading = false, chequePayload = result.data))
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
                                    paymentMethodsList = result.data
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