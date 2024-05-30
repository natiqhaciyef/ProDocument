package com.natiqhaciyef.domain.usecase.payment

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.payment.MappedPaymentChequeModel
import com.natiqhaciyef.common.model.mapped.MappedPaymentModel
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.data.mapper.toMappedModel
import com.natiqhaciyef.data.mapper.toResponse
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class StartPaymentUseCase @Inject constructor(
    paymentRepository: PaymentRepository
) : BaseUseCase<PaymentRepository, MappedPaymentModel, MappedPaymentChequeModel>(paymentRepository) {


    override fun operate(data: MappedPaymentModel): Flow<Resource<MappedPaymentChequeModel>> = flow{
        emit(Resource.loading(null))

        val request = data.toResponse()
        when(val result = repository.startPayment(request)){
            is NetworkResult.Success -> {
                emit(Resource.success(result.data.toMappedModel()))
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
                emit(Resource.error(
                    msg = result.e.message ?: ErrorMessages.SOMETHING_WENT_WRONG,
                    data = null,
                    exception = Exception(result.e),
                    errorCode = -1
                ))
            }
        }
    }
}