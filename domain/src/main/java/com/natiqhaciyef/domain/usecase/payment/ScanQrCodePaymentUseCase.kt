package com.natiqhaciyef.domain.usecase.payment

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedQrCodePaymentModel
import com.natiqhaciyef.common.model.payment.MappedSubscriptionPlanPaymentDetails
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toMapped
import com.natiqhaciyef.domain.mapper.toResponse
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import com.natiqhaciyef.domain.network.request.QrCodeRequest
import com.natiqhaciyef.domain.repository.PaymentRepository
import com.natiqhaciyef.domain.usecase.PICKED_SUBSCRIPTION_PLAN
import com.natiqhaciyef.domain.usecase.QR_CODE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class ScanQrCodePaymentUseCase @Inject constructor(
    paymentRepository: PaymentRepository
): BaseUseCase<PaymentRepository, Map<String, Any>, MappedQrCodePaymentModel>(paymentRepository) {

    override fun operate(data: Map<String, Any>): Flow<Resource<MappedQrCodePaymentModel>> {
        val qrCode = data[QR_CODE].toString()
        val planDetails = (data[PICKED_SUBSCRIPTION_PLAN] as MappedSubscriptionPlanPaymentDetails)
            .toResponse()

        val request = QrCodeRequest(
            qrCode = qrCode,
            subscriptionDetails = planDetails
        )

        return handleFlowResult(
            networkResult = { repository.scanQrCodePayment(request) },
            operation = { Resource.success(it.toMapped()) }
        )
    }
}