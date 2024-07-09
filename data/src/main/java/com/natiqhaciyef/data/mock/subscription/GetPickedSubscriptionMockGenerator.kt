package com.natiqhaciyef.data.mock.subscription

import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.response.SubscriptionResponse

class GetPickedSubscriptionMockGenerator(
    override var takenRequest: String
) : BaseMockGenerator<String, SubscriptionResponse>() {
    override var createdMock: SubscriptionResponse =
        SubscriptionMockManager.getPickedPlan(takenRequest)

    override fun getMock(
        action: ((String) -> SubscriptionResponse?)?
    ): SubscriptionResponse {
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

    companion object GetPickedSubscriptionMockGenerator{
        const val customRequest =  "steve@minecraft.com"
    }
}