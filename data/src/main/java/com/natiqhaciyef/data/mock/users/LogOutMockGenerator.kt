package com.natiqhaciyef.data.mock.users

import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator

class LogOutMockGenerator(
    override var takenRequest: Any
) : BaseMockGenerator<Any, CRUDResponse>() {
    override var createdMock: CRUDResponse =
        AccountMockManager.logout(EMPTY_STRING)


    override fun getMock(request: Any, action: (Any) -> CRUDResponse?): CRUDResponse {
        return createdMock
    }
}