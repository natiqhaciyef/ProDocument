package com.natiqhaciyef.data.source

import com.natiqhaciyef.core.base.mock.generateMockerClass
import com.natiqhaciyef.data.local.dao.PaymentDao
import com.natiqhaciyef.data.local.entity.PaymentEntity
import com.natiqhaciyef.data.mock.payment.GetAllSavedPaymentMethodsMockGenerator
import com.natiqhaciyef.data.mock.payment.GetChequePdfMockGenerator
import com.natiqhaciyef.data.mock.payment.GetPickedPaymentDetailsMockGenerator
import com.natiqhaciyef.data.mock.payment.InsertNewPaymentMethodMockGenerator
import com.natiqhaciyef.data.mock.payment.StartPaymentMockGenerator
import com.natiqhaciyef.data.network.LoadType
import com.natiqhaciyef.data.network.handleNetworkResponse
import com.natiqhaciyef.data.network.manager.TokenManager
import com.natiqhaciyef.data.network.request.PaymentModel
import com.natiqhaciyef.data.network.request.PaymentRequest
import com.natiqhaciyef.data.network.response.PaymentChequeResponse
import com.natiqhaciyef.data.network.response.PaymentPickModel
import com.natiqhaciyef.data.network.service.PaymentService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PaymentDataSource(
    private val manager: TokenManager,
    private val service: PaymentService,
    private val dao: PaymentDao
) {

    suspend fun getPaymentData(payment: PaymentRequest) = withContext(Dispatchers.IO) {
        val requestHeader = manager.generateToken()
        val mock = generateMockerClass(StartPaymentMockGenerator::class, payment)
            .getMock(StartPaymentMockGenerator.customRequest) { null }

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.getPaymentData(token = requestHeader, paymentData = payment)
        }
    }

    suspend fun startPayment(cheque: PaymentChequeResponse) = withContext(Dispatchers.IO){
        val requestHeader = manager.generateToken()

        handleNetworkResponse(mock = null, handlingType = LoadType.MOCK) {
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
                    paymentPickModel = paymentPickModel
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
                    paymentModel = paymentModel
                )
            }
        }


    suspend fun getStoredPaymentMethods() = withContext(Dispatchers.IO){
        dao.getStoredPaymentMethods()
    }

    suspend fun insertNewPaymentMethod(entity: PaymentEntity) = withContext(Dispatchers.IO){
        dao.insertNewPaymentMethod(entity)
    }

    suspend fun removePaymentMethod(details: String) = withContext(Dispatchers.IO){
        dao.removePaymentMethod(details)
    }
}