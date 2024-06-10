package com.natiqhaciyef.domain.usecase.payment.remote

import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.payment.MappedPaymentChequeModel
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.data.mapper.toModel
import com.natiqhaciyef.data.mapper.toResponse
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class InsertPaymentChequeUseCase @Inject constructor(
    paymentRepository: PaymentRepository
) : BaseUseCase<PaymentRepository, MappedPaymentChequeModel, CRUDModel>(paymentRepository) {

    override fun operate(data: MappedPaymentChequeModel): Flow<Resource<CRUDModel>> = flow {
        emit(Resource.loading(null))
        val request = data.toResponse()

        when(val result = repository.insertPaymentCheque(request)){
            is NetworkResult.Success -> {
                val mapped = result.data.toModel()

                if (mapped.resultCode in 200..299)
                    emit(Resource.success(mapped))
                else
                    emit(Resource.error(
                        msg = mapped.message,
                        data = mapped,
                        exception = Exception(mapped.message),
                        errorCode = mapped.resultCode
                    ))
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