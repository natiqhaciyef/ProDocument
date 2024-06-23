package com.natiqhaciyef.data.mock.appdetails

import com.natiqhaciyef.common.model.FaqModel
import com.natiqhaciyef.core.base.mock.BaseMockGenerator

class GetFaqListMockGenerator(
    override var takenRequest: Unit
) : BaseMockGenerator<Unit, List<FaqModel>>() {
    override var createdMock: List<FaqModel> =
        AppDetailsMockManager.getFaqList()

    override fun getMock(request: Unit, action: (Unit) -> List<FaqModel>?): List<FaqModel> {
        return createdMock
    }
}