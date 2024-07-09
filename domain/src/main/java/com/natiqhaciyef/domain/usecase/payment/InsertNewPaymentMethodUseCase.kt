package com.natiqhaciyef.domain.usecase.payment

import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.payment.MappedPaymentModel
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toModel
import com.natiqhaciyef.domain.mapper.toResponse
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import com.natiqhaciyef.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class InsertNewPaymentMethodUseCase @Inject constructor(
    paymentRepository: PaymentRepository
) : BaseUseCase<PaymentRepository, MappedPaymentModel, CRUDModel>(paymentRepository) {

    override fun operate(data: MappedPaymentModel): Flow<Resource<CRUDModel>> {
        val request = data.toResponse()

        return handleFlowResult(
            networkResult = { repository.insertNewPaymentMethod(request) },
            operation = { Resource.success(it.toModel()) }
        )
    }

}