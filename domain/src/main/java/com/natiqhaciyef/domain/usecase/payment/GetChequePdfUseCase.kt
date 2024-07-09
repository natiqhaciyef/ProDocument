package com.natiqhaciyef.domain.usecase.payment

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import com.natiqhaciyef.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class GetChequePdfUseCase @Inject constructor(
    paymentRepository: PaymentRepository
): BaseUseCase<PaymentRepository, String, String>(paymentRepository) {

    override fun operate(data: String): Flow<Resource<String>> {
        return handleFlowResult(
            networkResult = { repository.getChequePdf(data) },
            operation = { Resource.success(it.payload) }
        )
    }
}