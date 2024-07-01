package com.natiqhaciyef.data.source

import com.natiqhaciyef.core.base.mock.generateMockerClass
import com.natiqhaciyef.data.mock.payment.GetAllSavedPaymentMethodsMockGenerator
import com.natiqhaciyef.data.mock.payment.GetChequePdfMockGenerator
import com.natiqhaciyef.data.mock.payment.GetPickedPaymentDetailsMockGenerator
import com.natiqhaciyef.data.mock.payment.InsertNewPaymentMethodMockGenerator
import com.natiqhaciyef.data.mock.payment.GetPaymentDataPaymentMockGenerator
import com.natiqhaciyef.data.mock.payment.GetPaymentHistoryDetailsMockGenerator
import com.natiqhaciyef.data.mock.payment.GetPaymentHistoryMockGenerator
import com.natiqhaciyef.data.mock.payment.ScanQrCodePaymentMockGenerator
import com.natiqhaciyef.data.mock.payment.StartPaymentMockGenerator
import com.natiqhaciyef.domain.network.LoadType
import com.natiqhaciyef.domain.network.handleNetworkResponse
import com.natiqhaciyef.data.network.manager.TokenManager
import com.natiqhaciyef.domain.network.request.PaymentModel
import com.natiqhaciyef.domain.network.request.PaymentRequest
import com.natiqhaciyef.domain.network.request.QrCodeRequest
import com.natiqhaciyef.domain.network.response.PaymentChequeResponse
import com.natiqhaciyef.domain.network.response.PaymentPickModel
import com.natiqhaciyef.data.network.service.PaymentService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PaymentDataSource @Inject constructor(
    private val manager: TokenManager,
    private val service: PaymentService
) {

    suspend fun getPaymentData(payment: PaymentRequest) = withContext(Dispatchers.IO) {
        val requestHeader = manager.generateToken()
        val mock = generateMockerClass(GetPaymentDataPaymentMockGenerator::class, payment)
            .getMock(GetPaymentDataPaymentMockGenerator.customRequest) { null }

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.getPaymentData(token = requestHeader, paymentData = payment)
        }
    }

    suspend fun startPayment(cheque: PaymentChequeResponse) = withContext(Dispatchers.IO){
        val requestHeader = manager.generateToken()
        val mock = generateMockerClass(StartPaymentMockGenerator::class, cheque)
            .getMock(StartPaymentMockGenerator.customRequest) { null }

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.startPayment(token = requestHeader, cheque = cheque)
        }
    }

    suspend fun getChequePdf(checkId: String) = withContext(Dispatchers.IO) {
        val requestHeader = manager.generateToken()
        val mock = generateMockerClass(GetChequePdfMockGenerator::class, checkId)
            .getMock(GetChequePdfMockGenerator.CUSTOM_REQUEST) { null }

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.getChequePdf(token = requestHeader, checkId = checkId)
        }
    }

    suspend fun getAllSavedPaymentMethods() = withContext(Dispatchers.IO) {
        val requestHeader = manager.generateToken()
        val mock = generateMockerClass(GetAllSavedPaymentMethodsMockGenerator::class, Unit)
            .getMock(Unit) { null }

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.getAllSavedPaymentMethods(token = requestHeader)
        }
    }

    suspend fun getPickedPaymentDetails(paymentPickModel: PaymentPickModel) =
        withContext(Dispatchers.IO) {
            val requestHeader = manager.generateToken()
            val mock =
                generateMockerClass(GetPickedPaymentDetailsMockGenerator::class, paymentPickModel)
                    .getMock(GetPickedPaymentDetailsMockGenerator.customRequest) { null }

            handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
                service.getPickedPaymentDetails(
                    token = requestHeader,
                    pickedPayment = paymentPickModel
                )
            }
        }

    suspend fun insertNewPaymentMethod(paymentModel: PaymentModel) =
        withContext(Dispatchers.IO) {
            val requestHeader = manager.generateToken()
            val mock =
                generateMockerClass(InsertNewPaymentMethodMockGenerator::class, paymentModel)
                    .getMock(InsertNewPaymentMethodMockGenerator.customRequest) { null }

            handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
                service.insertNewPaymentMethod(
                    token = requestHeader,
                    payment = paymentModel
                )
            }
        }

    suspend fun getPaymentHistory() =
        withContext(Dispatchers.IO) {
            val requestHeader = manager.generateToken()
            val mock = generateMockerClass(GetPaymentHistoryMockGenerator::class, Unit)
                .getMock(Unit){ null }

            handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
                service.getPaymentHistory(token = requestHeader)
            }
        }

    suspend fun getPaymentHistoryDetails(chequeId: String) =
        withContext(Dispatchers.IO){
            val requestHeader = manager.generateToken()
            val mock = generateMockerClass(GetPaymentHistoryDetailsMockGenerator::class, chequeId)
                .getMock(GetPaymentHistoryDetailsMockGenerator.customRequest) { null }

            handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
                service.getPaymentHistoryDetails(token = requestHeader, chequeId = chequeId)
            }
        }

    suspend fun scanQrCodePayment(qrCodeRequest: QrCodeRequest) =
        withContext(Dispatchers.IO) {
            val requestHeader = manager.generateToken()
            val mock = generateMockerClass(ScanQrCodePaymentMockGenerator::class, qrCodeRequest)
                .getMock(ScanQrCodePaymentMockGenerator.customRequest) { null }

            handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
                service.scanQrCodePayment(token = requestHeader, qrCodeRequest = qrCodeRequest)
            }
        }
}