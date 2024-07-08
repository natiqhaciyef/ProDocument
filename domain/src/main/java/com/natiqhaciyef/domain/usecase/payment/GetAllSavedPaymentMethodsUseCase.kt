package com.natiqhaciyef.domain.usecase.payment

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.payment.MappedPaymentModel
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toMapped
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import com.natiqhaciyef.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class GetAllSavedPaymentMethodsUseCase @Inject constructor(
    paymentRepository: PaymentRepository
): BaseUseCase<PaymentRepository, Unit, List<MappedPaymentModel>>(paymentRepository) {

    override fun invoke(): Flow<Resource<List<MappedPaymentModel>>>{
        return handleFlowResult(
            networkResult = { repository.getAllSavedPaymentMethods() },
            operation = { Resource.success(it.map { it.toMapped() }) }
        )
    }

}