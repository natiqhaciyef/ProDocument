package com.natiqhaciyef.domain.usecase.payment.local

import com.natiqhaciyef.common.constants.FOUR_HUNDRED_FOUR
import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.payment.MappedPaymentModel
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.data.mapper.toEntity
import com.natiqhaciyef.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.Exception

@UseCase
class InsertNewPaymentMethodUseCase @Inject constructor(
    paymentRepository: PaymentRepository
) : BaseUseCase<PaymentRepository, MappedPaymentModel, Unit>(paymentRepository) {

    override fun run(data: MappedPaymentModel): Flow<Resource<Boolean>> = flow {
        emit(Resource.loading(null))

        try {
            repository.insertNewPaymentMethod(data.toEntity())
            emit(Resource.success(true))
        } catch (e: Exception) {
            emit(
                Resource.error(
                    msg = SOMETHING_WENT_WRONG,
                    data = null,
                    exception = e,
                    errorCode = "$FOUR_HUNDRED_FOUR$ONE".toInt()
                )
            )
        }
    }

}