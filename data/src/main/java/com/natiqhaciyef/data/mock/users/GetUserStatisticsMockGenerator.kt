package com.natiqhaciyef.data.mock.users

import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.mock.users.AccountMockManager.EMAIL_MOCK
import com.natiqhaciyef.domain.network.response.GraphDetailModel
import com.natiqhaciyef.domain.network.response.GraphDetailsListResponse

class GetUserStatisticsMockGenerator(
    override var takenRequest: Unit
) : BaseMockGenerator<Unit, GraphDetailsListResponse>() {
    override var createdMock: GraphDetailsListResponse =
        AccountMockManager.getStatistics(EMAIL_MOCK)

    override fun getMock(
        action: ((Unit) -> GraphDetailsListResponse?)?
    ): GraphDetailsListResponse {
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