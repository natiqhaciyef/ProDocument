package com.natiqhaciyef.domain.usecase.payment

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.payment.MappedPaymentModel
import com.natiqhaciyef.common.model.payment.MappedPaymentPickModel
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toMapped
import com.natiqhaciyef.domain.mapper.toResponse
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import com.natiqhaciyef.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class GetPickedPaymentDetailsUseCase @Inject constructor(
    paymentRepository: PaymentRepository
): BaseUseCase<PaymentRepository, MappedPaymentPickModel, MappedPaymentModel>(paymentRepository) {

    override fun operate(data: MappedPaymentPickModel): Flow<Resource<MappedPaymentModel>> {
        val request = data.toResponse()

        return handleFlowResult(
            networkResult = { repository.getPickedPaymentDetails(request) },
            operation = { Resource.success(it.toMapped()) }
        )
    }

}