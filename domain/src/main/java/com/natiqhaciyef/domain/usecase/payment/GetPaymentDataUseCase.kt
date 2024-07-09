package com.natiqhaciyef.domain.usecase.payment

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.payment.MappedPaymentChequeModel
import com.natiqhaciyef.common.model.payment.MappedPaymentModel
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toMapped
import com.natiqhaciyef.domain.mapper.toResponse
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import com.natiqhaciyef.domain.network.request.PaymentRequest
import com.natiqhaciyef.domain.repository.PaymentRepository
import com.natiqhaciyef.domain.usecase.PAYMENT_MODEL
import com.natiqhaciyef.domain.usecase.PICKED_SUBSCRIPTION_PLAN
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class GetPaymentDataUseCase @Inject constructor(
    paymentRepository: PaymentRepository
) : BaseUseCase<PaymentRepository, Map<String, Any>, MappedPaymentChequeModel>(paymentRepository) {

    override fun operate(data: Map<String, Any>): Flow<Resource<MappedPaymentChequeModel>> {
        val payment = (data[PAYMENT_MODEL] as MappedPaymentModel).toResponse()
        val pickedPlan = data[PICKED_SUBSCRIPTION_PLAN].toString()
        val request = PaymentRequest(
            merchantId = payment.merchantId,
            paymentType = payment.paymentType,
            paymentMethod = payment.paymentMethod,
            paymentDetails = payment.paymentDetails,
            pickedPlanToken = pickedPlan
        )

        return handleFlowResult(
            networkResult = { repository.getPaymentData(request) },
            operation = { Resource.success(it.toMapped()) }
        )
    }
}