package com.natiqhaciyef.data.mock.payment

import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.response.ChequePayloadModel

class GetChequePdfMockGenerator(
    override var takenRequest: String
) : BaseMockGenerator<String, ChequePayloadModel>() {
    override var createdMock: ChequePayloadModel =
        ChequePayloadModel(payload = "Payload")

    override fun getMock(
        request: String,
        action: (String) -> ChequePayloadModel?
    ): ChequePayloadModel {
        return PaymentMockManager.getChequePayload(takenRequest) ?: createdMock
    }

    companion object GetChequePdfMockGenerator{
        const val CUSTOM_REQUEST = "Request for payload"
    }
}