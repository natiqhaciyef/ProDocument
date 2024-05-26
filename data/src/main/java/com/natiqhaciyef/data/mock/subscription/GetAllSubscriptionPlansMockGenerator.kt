package com.natiqhaciyef.data.mock.subscription

import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.response.SubscriptionResponse

class GetAllSubscriptionPlansMockGenerator(
    override var takenRequest: Unit
) : BaseMockGenerator<Unit, List<SubscriptionResponse>>() {
    override var createdMock: List<SubscriptionResponse> = listOf(
        SubscriptionResponse(
            title = "Empty",
            price = 0.0,
            perTime = 1,
            timeType = "day",
            description = "no description",
            features = listOf("Nothing", "Empty", "Unit", "Void"),
            expireDate = "0.00.0",
            backgroundColor = "red",
            token = "subscriptionToken"
        )
    )

    override fun getMock(
        request: Unit,
        action: (Unit) -> List<SubscriptionResponse>?
    ): List<SubscriptionResponse> {
        return createdMock
    }
}