package com.natiqhaciyef.data.mock.subscription

import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator

class ActivatePickedPlanMockGenerator(
    override var takenRequest: String
) : BaseMockGenerator<String, CRUDResponse>(){
    override var createdMock: CRUDResponse =
        CRUDResponse(
            resultCode = 299,
            message = "Mock crud"
        )

    override fun getMock(request: String, action: (String) -> CRUDResponse?): CRUDResponse {
//        return if (request == takenRequest)
//            createdMock
//        else
//            action.invoke(request) ?: throw Companion.MockRequestException()
        return createdMock
    }

    companion object ActivatePickedPlanMockGenerator{
        const val customRequest = "subscriptionTokenMock"
    }
}