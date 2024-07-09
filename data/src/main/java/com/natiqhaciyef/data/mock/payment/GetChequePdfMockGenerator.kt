package com.natiqhaciyef.data.mock.payment

import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.response.ChequePayloadModel

class GetChequePdfMockGenerator(
    override var takenRequest: String
) : BaseMockGenerator<String, ChequePayloadModel>() {
    override var createdMock: ChequePayloadModel =
        PaymentMockManager.getChequePayload(takenRequest)

    override fun getMock(
        action: ((String) -> ChequePayloadModel?)?
    ): ChequePayloadModel {
        if (action != null)
            try {
                return action.invoke(takenRequest) ?: throw Companion.MockRequestException(
                    MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
                )
            } catch (e: Exception) {
                println(e)
            }

        return createdMock
    }

    companion object GetChequePdfMockGenerator{
        const val CUSTOM_REQUEST = "Request for payload"
    }
}