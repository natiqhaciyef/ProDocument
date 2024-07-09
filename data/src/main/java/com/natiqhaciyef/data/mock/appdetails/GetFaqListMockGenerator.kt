package com.natiqhaciyef.data.mock.appdetails

import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
import com.natiqhaciyef.common.model.FaqModel
import com.natiqhaciyef.core.base.mock.BaseMockGenerator

class GetFaqListMockGenerator(
    override var takenRequest: Unit
) : BaseMockGenerator<Unit, List<FaqModel>>() {
    override var createdMock: List<FaqModel> =
        AppDetailsMockManager.getFaqList()

    override fun getMock(
        action: ((Unit) -> List<FaqModel>?)?
    ): List<FaqModel> {
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