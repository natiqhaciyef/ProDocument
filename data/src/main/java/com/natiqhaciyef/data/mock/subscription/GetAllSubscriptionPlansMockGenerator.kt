package com.natiqhaciyef.data.mock.subscription

import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.response.SubscriptionResponse

class GetAllSubscriptionPlansMockGenerator(
    override var takenRequest: Unit
) : BaseMockGenerator<Unit, List<SubscriptionResponse>>() {
    override var createdMock: List<SubscriptionResponse> =
        SubscriptionMockManager.getAllSubscriptions()

    override fun getMock(
        request: Unit,
        action: (Unit) -> List<SubscriptionResponse>?
    ): List<SubscriptionResponse> {
        return createdMock
    }
}