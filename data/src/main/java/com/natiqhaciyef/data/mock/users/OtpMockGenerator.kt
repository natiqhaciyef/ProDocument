package com.natiqhaciyef.data.mock.users

import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator

class OtpMockGenerator(
    override var takenRequest: String
) : BaseMockGenerator<String, CRUDResponse>() {
    override var createdMock: CRUDResponse =
        AccountMockManager.otpCheck(takenRequest)

    override fun getMock(
        action: ((String) -> CRUDResponse?)?
    ): CRUDResponse {
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
}