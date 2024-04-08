package com.natiqhaciyef.data.mock.users

import com.natiqhaciyef.data.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.response.CRUDResponse

class LogOutMockGenerator(
    override var takenRequest: Any? = null
) : BaseMockGenerator<Any?, CRUDResponse>() {
    override var createdMock: CRUDResponse =
        CRUDResponse(
            resultCode = 299,
            message = "Mock crud"
        )


    override fun getMock(request: Any?, action: (Any?) -> CRUDResponse?): CRUDResponse {
        return createdMock
    }
}