package com.natiqhaciyef.domain.usecase.payment

import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.payment.MappedPaymentChequeModel
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.constants.TWO_HUNDRED
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.common.constants.UNKNOWN_ERROR
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toModel
import com.natiqhaciyef.domain.mapper.toResponse
import com.natiqhaciyef.domain.network.NetworkResult
import com.natiqhaciyef.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class StartPaymentUseCase @Inject constructor(
    paymentRepository: PaymentRepository
): BaseUseCase<PaymentRepository, MappedPaymentChequeModel, CRUDModel>(paymentRepository){

    override fun operate(data: MappedPaymentChequeModel): Flow<Resource<CRUDModel>> = flow {
        emit(Resource.loading(null))

        val request = data.toResponse()
        when (val result = repository.startPayment(request)) {
            is NetworkResult.Success -> {
                val mapped = result.data.toModel()
                if (mapped.resultCode in TWO_HUNDRED..TWO_HUNDRED_NINETY_NINE)
                    emit(Resource.success(mapped))
                else
                    emit(
                        Resource.error(
                            msg = mapped.message,
                            data = mapped,
                            exception = Exception(mapped.message),
                            errorCode = mapped.resultCode
                        )
                    )
            }

            is NetworkResult.Error -> {
                emit(
                    Resource.error(
                        msg = result.message ?: UNKNOWN_ERROR,
                        data = null,
                        exception = Exception(result.message),
                        errorCode = result.code
                    )
                )
            }

            is NetworkResult.Exception -> {
                emit(
                    Resource.error(
                        msg = result.e.message ?: SOMETHING_WENT_WRONG,
                        data = null,
                        exception = Exception(result.e),
                        errorCode = -ONE
                    )
                )
            }
        }
    }
}