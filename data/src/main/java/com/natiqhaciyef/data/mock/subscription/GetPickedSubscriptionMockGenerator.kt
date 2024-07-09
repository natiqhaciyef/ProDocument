package com.natiqhaciyef.data.mock.subscription

import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.response.SubscriptionResponse

class GetPickedSubscriptionMockGenerator(
    override var takenRequest: String
) : BaseMockGenerator<String, SubscriptionResponse>() {
    override var createdMock: SubscriptionResponse =
        SubscriptionResponse(
            title = "Week unit",
            price = 4.99,
            perTime = 1,
            timeType = "week",
            description = "no description",
            features = listOf("Something", "Empty", "Unit", "Void", "No limit"),
            expireDate = "0.00.0",
            backgroundColor = "orange",
            size = 1024,
            sizeType = "MB",
            token = "subscriptionToken-3"
        )

    override fun getMock(
        request: String,
        action: (String) -> SubscriptionResponse?
    ): SubscriptionResponse {
        if (request == takenRequest)
            return createdMock

        return SubscriptionMockManager.getPickedPlan(takenRequest)
    }

    companion object GetPickedSubscriptionMockGenerator{
        const val customRequest =  "steve@minecraft.com"
    }
}