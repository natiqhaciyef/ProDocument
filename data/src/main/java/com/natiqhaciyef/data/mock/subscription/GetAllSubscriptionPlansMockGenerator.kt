package com.natiqhaciyef.data.mock.subscription

import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.response.SubscriptionResponse

class GetAllSubscriptionPlansMockGenerator(
    override var takenRequest: Unit
) : BaseMockGenerator<Unit, List<SubscriptionResponse>>() {
    override var createdMock: List<SubscriptionResponse> =
        SubscriptionMockManager.getAllSubscriptions()

    override fun getMock(
        action: ((Unit) -> List<SubscriptionResponse>?)?
    ): List<SubscriptionResponse> {
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