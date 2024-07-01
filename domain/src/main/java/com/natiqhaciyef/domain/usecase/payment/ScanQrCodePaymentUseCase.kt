package com.natiqhaciyef.domain.usecase.payment

import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedQrCodePaymentModel
import com.natiqhaciyef.common.model.payment.MappedSubscriptionPlanPaymentDetails
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.constants.UNKNOWN_ERROR
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toMapped
import com.natiqhaciyef.domain.mapper.toResponse
import com.natiqhaciyef.domain.network.NetworkResult
import com.natiqhaciyef.domain.network.request.QrCodeRequest
import com.natiqhaciyef.domain.repository.PaymentRepository
import com.natiqhaciyef.domain.usecase.PICKED_SUBSCRIPTION_PLAN
import com.natiqhaciyef.domain.usecase.QR_CODE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class ScanQrCodePaymentUseCase @Inject constructor(
    paymentRepository: PaymentRepository
): BaseUseCase<PaymentRepository, Map<String, Any>, MappedQrCodePaymentModel>(paymentRepository) {

    override fun operate(data: Map<String, Any>): Flow<Resource<MappedQrCodePaymentModel>> = flow{
        emit(Resource.loading())

        val qrCode = data[QR_CODE].toString()
        val planDetails = (data[PICKED_SUBSCRIPTION_PLAN] as MappedSubscriptionPlanPaymentDetails)
            .toResponse()

        val request = QrCodeRequest(
            qrCode = qrCode,
            subscriptionDetails = planDetails
        )

        when(val result = repository.scanQrCodePayment(request)){
            is NetworkResult.Success -> {
                val mapped = result.data.toMapped()
                emit(Resource.success(mapped))
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