package com.natiqhaciyef.domain.usecase.payment

import com.natiqhaciyef.common.constants.EMPTY_LIST
import com.natiqhaciyef.common.constants.FOUR_HUNDRED_ELEVEN
import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.constants.UNKNOWN_ERROR
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.payment.PaymentHistoryModel
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toPaymentHistory
import com.natiqhaciyef.domain.network.NetworkResult
import com.natiqhaciyef.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class GetPaymentHistoryUseCase @Inject constructor(
    paymentRepository: PaymentRepository
) : BaseUseCase<PaymentRepository, Unit, List<PaymentHistoryModel>>(paymentRepository) {

    override fun invoke(): Flow<Resource<List<PaymentHistoryModel>>> = flow {
        emit(Resource.loading(null))

        when (val result = repository.getPaymentHistory()) {
            is NetworkResult.Success -> {
                val mappedList = result.data.map { it.toPaymentHistory() }

                if (mappedList.isNotEmpty())
                    emit(Resource.success(mappedList))
                else
                    emit(
                        Resource.error(
                            msg = EMPTY_LIST,
                            data = null,
                            exception = Exception(EMPTY_LIST),
                            errorCode = FOUR_HUNDRED_ELEVEN
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