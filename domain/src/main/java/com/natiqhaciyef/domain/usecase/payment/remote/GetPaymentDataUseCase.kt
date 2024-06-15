package com.natiqhaciyef.domain.usecase.payment.remote

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.payment.MappedPaymentChequeModel
import com.natiqhaciyef.common.model.payment.MappedPaymentModel
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.data.mapper.toMapped
import com.natiqhaciyef.data.mapper.toResponse
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.data.network.request.PaymentRequest
import com.natiqhaciyef.domain.repository.PaymentRepository
import com.natiqhaciyef.domain.usecase.PAYMENT_MODEL
import com.natiqhaciyef.domain.usecase.PICKED_SUBSCRIPTION_PLAN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class GetPaymentDataUseCase @Inject constructor(
    paymentRepository: PaymentRepository
) : BaseUseCase<PaymentRepository, Map<String, Any>, MappedPaymentChequeModel>(paymentRepository) {

    override fun operate(data: Map<String, Any>): Flow<Resource<MappedPaymentChequeModel>> = flow {
        emit(Resource.loading(null))

        val payment = (data[PAYMENT_MODEL] as MappedPaymentModel).toResponse()
        val pickedPlan = data[PICKED_SUBSCRIPTION_PLAN].toString()
        val request = PaymentRequest(
            merchantId = payment.merchantId,
            paymentType = payment.paymentType,
            paymentMethod = payment.paymentMethod,
            paymentDetails = payment.paymentDetails,
            pickedPlanToken = pickedPlan
        )

        when (val result = repository.getPaymentData(request)) {
            is NetworkResult.Success -> {
                emit(Resource.success(result.data.toMapped()))
            }

            is NetworkResult.Error -> {
                emit(
                    Resource.error(
                        msg = result.message ?: ErrorMessages.UNKNOWN_ERROR,
                        data = null,
                        exception = Exception(result.message),
                        errorCode = result.code
                    )
                )
            }

            is NetworkResult.Exception -> {
                emit(
                    Resource.error(
                        msg = result.e.message ?: ErrorMessages.SOMETHING_WENT_WRONG,
                        data = null,
                        exception = Exception(result.e),
                        errorCode = -1
                    )
                )
            }
        }
    }
}