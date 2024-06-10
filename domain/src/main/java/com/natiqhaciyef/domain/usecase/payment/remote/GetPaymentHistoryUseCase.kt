package com.natiqhaciyef.domain.usecase.payment.remote

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.payment.MappedPaymentChequeModel
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.data.mapper.toMapped
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class GetPaymentHistoryUseCase @Inject constructor(
    paymentRepository: PaymentRepository
) : BaseUseCase<PaymentRepository, Unit, List<MappedPaymentChequeModel>>(paymentRepository) {

    override fun invoke(): Flow<Resource<List<MappedPaymentChequeModel>>> = flow {
        emit(Resource.loading(null))

        when (val result = repository.getPaymentHistory()) {
            is NetworkResult.Success -> {
                val mappedList = result.data.map { it.toMapped() }

                if (mappedList.isNotEmpty())
                    emit(Resource.success(mappedList))
                else
                    emit(
                        Resource.error(
                            msg = ErrorMessages.EMPTY_LIST,
                            data = null,
                            exception = Exception(ErrorMessages.EMPTY_LIST),
                            errorCode = 411
                        )
                    )
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