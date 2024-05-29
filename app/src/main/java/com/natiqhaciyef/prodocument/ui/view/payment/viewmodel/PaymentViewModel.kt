package com.natiqhaciyef.prodocument.ui.view.payment.viewmodel

import com.natiqhaciyef.core.base.ui.BaseViewModel
import com.natiqhaciyef.prodocument.ui.view.payment.contract.PaymentContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(

): BaseViewModel<PaymentContract.PaymentState, PaymentContract.PaymentEvent, PaymentContract.PaymentEffect>() {

    override fun onEventUpdate(event: PaymentContract.PaymentEvent) {
        when(event){
            is PaymentContract.PaymentEvent.PickPaymentMethod -> { pickPayment() }
            is PaymentContract.PaymentEvent.AddNewPaymentMethod -> { insertNewPaymentMethod() }
            is PaymentContract.PaymentEvent.PayForPlan -> { payForPlan() }
            is PaymentContract.PaymentEvent.GetAllStoredPaymentMethods -> { payForPlan() }
        }
    }

    private fun getAllStoredMethods(){
        // TODO get all dataset list from room or other saved places
    }

    private fun pickPayment(){
        // TODO get dataset from room or other saved places
    }

    private fun insertNewPaymentMethod(){
        // TODO save dataset to room or other saved places
    }

    private fun payForPlan(){
        // TODO pay now
    }

    override fun getInitialState(): PaymentContract.PaymentState =
        PaymentContract.PaymentState()
}