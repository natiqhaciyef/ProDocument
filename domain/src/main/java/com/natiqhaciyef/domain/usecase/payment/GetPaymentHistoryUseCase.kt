package com.natiqhaciyef.domain.usecase.payment

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.payment.PaymentHistoryModel
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toPaymentHistory
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import com.natiqhaciyef.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class GetPaymentHistoryUseCase @Inject constructor(
    paymentRepository: PaymentRepository
) : BaseUseCase<PaymentRepository, Unit, List<PaymentHistoryModel>>(paymentRepository) {

    override fun invoke(): Flow<Resource<List<PaymentHistoryModel>>> {
        return handleFlowResult(
            networkResult = { repository.getPaymentHistory() },
            operation = { Resource.success(it.map { it.toPaymentHistory() }) }
        )
    }
}