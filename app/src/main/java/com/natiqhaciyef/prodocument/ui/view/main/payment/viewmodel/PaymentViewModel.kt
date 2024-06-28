package com.natiqhaciyef.prodocument.ui.view.main.payment.viewmodel

import android.content.Context
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.natiqhaciyef.common.model.Status
import com.natiqhaciyef.common.model.payment.MappedPaymentChequeModel
import com.natiqhaciyef.common.model.payment.MappedPaymentModel
import com.natiqhaciyef.common.model.payment.MappedPaymentPickModel
import com.natiqhaciyef.common.model.payment.MappedSubscriptionPlanPaymentDetails
import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.domain.usecase.PAYMENT_MODEL
import com.natiqhaciyef.domain.usecase.PICKED_SUBSCRIPTION_PLAN
import com.natiqhaciyef.domain.usecase.QR_CODE
import com.natiqhaciyef.domain.usecase.payment.remote.GetAllSavedPaymentMethodsUseCase
import com.natiqhaciyef.domain.usecase.payment.remote.GetChequePdfUseCase
import com.natiqhaciyef.domain.usecase.payment.remote.GetPaymentDataUseCase
import com.natiqhaciyef.domain.usecase.payment.remote.GetPickedPaymentDetailsUseCase
import com.natiqhaciyef.domain.usecase.payment.remote.ScanQrCodePaymentUseCase
import com.natiqhaciyef.domain.usecase.payment.remote.StartPaymentUseCase
import com.natiqhaciyef.prodocument.ui.manager.CameraManager
import com.natiqhaciyef.prodocument.ui.view.main.payment.contract.PaymentContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.concurrent.Flow.Subscription
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val getAllSavedPaymentMethodsUseCase: GetAllSavedPaymentMethodsUseCase,
    private val insertNewPaymentMethodRemoteUseCase: com.natiqhaciyef.domain.usecase.payment.remote.InsertNewPaymentMethodUseCase,
    private val startPaymentUseCase: StartPaymentUseCase,
    private val getPaymentDataUseCase: GetPaymentDataUseCase,
    private val getChequePdfUseCase: GetChequePdfUseCase,
    private val scanQrCodePaymentUseCase: ScanQrCodePaymentUseCase
) : BaseViewModel<PaymentContract.PaymentState, PaymentContract.PaymentEvent, PaymentContract.PaymentEffect>() {
    private val cameraReaderLiveData = MutableLiveData<CameraManager?>(null)

    @ExperimentalGetImage
    override fun onEventUpdate(event: PaymentContract.PaymentEvent) {
        when (event) {

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

            is PaymentContract.PaymentEvent.StartCamera -> {
                startCamera(event.context, event.lifecycle, event.preview, event.onSuccess)
            }

            is PaymentContract.PaymentEvent.ScanQRCode -> {
                scanQrCode(event.qrCode, event.subscriptionPlanPaymentDetails)
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

    @ExperimentalGetImage
    private fun startCamera(
        context: Context,
        lifecycle: LifecycleOwner,
        preview: PreviewView,
        onSuccess: (Any) -> Unit = { }
    ) {
        if (cameraReaderLiveData.value == null) {
            cameraReaderLiveData.value = CameraManager(context, lifecycle)
        }

        cameraReaderLiveData.value?.openBarcodeScanner(preview, onSuccess)
    }

    private fun scanQrCode(qrCode: String, planDetails: MappedSubscriptionPlanPaymentDetails){
        val map = mapOf(QR_CODE to qrCode, PICKED_SUBSCRIPTION_PLAN to planDetails)
        scanQrCodePaymentUseCase.operate(map).onEach { result ->
            when(result.status){
                Status.SUCCESS -> {
                    setBaseState(getCurrentBaseState().copy(isLoading = false, qrCodePaymentModel = result.data))
                }

                Status.ERROR -> {
                    setBaseState(getCurrentBaseState().copy(isLoading = false))
                }

                Status.LOADING -> {
                    setBaseState(getCurrentBaseState().copy(isLoading = true))
                }
            }
        }.launchIn(viewModelScope)
    }


    override fun getInitialState(): PaymentContract.PaymentState =
        PaymentContract.PaymentState()
}