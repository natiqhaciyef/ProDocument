package com.natiqhaciyef.data.mock.users

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
        request: Unit,
        action: (Unit) -> GraphDetailsListResponse?
    ): GraphDetailsListResponse {
        return createdMock
    }
}