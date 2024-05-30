package com.natiqhaciyef.data.mock.payment

import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.response.ChequePayloadModel

class GetChequePdfMockGenerator(
    override var takenRequest: String
) : BaseMockGenerator<String, ChequePayloadModel>() {
    override var createdMock: ChequePayloadModel =
        ChequePayloadModel(payload = "Payload")

    override fun getMock(
        request: String,
        action: (String) -> ChequePayloadModel?
    ): ChequePayloadModel {
        return if (request == takenRequest)
            createdMock
        else
            action.invoke(takenRequest) ?: throw Companion.MockRequestException()
    }

    companion object GetChequePdfMockGenerator{
        const val CUSTOM_REQUEST = "Request for payload"
    }
}