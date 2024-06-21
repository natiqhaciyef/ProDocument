package com.natiqhaciyef.domain.usecase.payment.remote

import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.constants.UNKNOWN_ERROR
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class GetChequePdfUseCase @Inject constructor(
    paymentRepository: PaymentRepository
): BaseUseCase<PaymentRepository, String, String>(paymentRepository) {

    override fun operate(data: String): Flow<Resource<String>> = flow{
        emit(Resource.loading(null))

        when(val result = repository.getChequePdf(data)){
            is NetworkResult.Success -> {
                emit(Resource.success(data = result.data.payload))
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
                emit(Resource.error(
                    msg = result.e.message ?: SOMETHING_WENT_WRONG,
                    data = null,
                    exception = Exception(result.e),
                    errorCode = -ONE
                ))
            }
        }
    }
}