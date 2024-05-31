package com.natiqhaciyef.domain.usecase.payment.local

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.payment.MappedPaymentModel
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.data.mapper.toMapped
import com.natiqhaciyef.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class GetStoredPaymentMethodsUseCase @Inject constructor(
    paymentRepository: PaymentRepository
): BaseUseCase<PaymentRepository, Unit, List<MappedPaymentModel>>(paymentRepository) {

    override fun invoke(): Flow<Resource<List<MappedPaymentModel>>> = flow {
        emit(Resource.loading(null))

        val result = repository.getStoredPaymentMethods()

        if (result.isNotEmpty()){
            emit(Resource.success(result.map { it.toMapped() }))
        }else{
            emit(Resource.error(
                msg = ErrorMessages.EMPTY_FIELD,
                data = null,
                exception = Exception(ErrorMessages.EMPTY_FIELD),
                errorCode = 4040
            ))
        }
    }

}