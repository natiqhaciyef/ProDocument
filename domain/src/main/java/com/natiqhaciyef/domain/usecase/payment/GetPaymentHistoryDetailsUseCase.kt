package com.natiqhaciyef.domain.usecase.payment

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.payment.MappedPaymentChequeModel
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toMapped
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import com.natiqhaciyef.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class GetPaymentHistoryDetailsUseCase @Inject constructor(
    paymentRepository: PaymentRepository
): BaseUseCase<PaymentRepository, String, MappedPaymentChequeModel>(paymentRepository) {

    override fun operate(data: String): Flow<Resource<MappedPaymentChequeModel>>{
        return handleFlowResult(
            networkResult = { repository.getPaymentHistoryDetails(data) },
            operation = { Resource.success(it.toMapped()) }
        )
    }
}