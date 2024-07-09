package com.natiqhaciyef.data.mock.appdetails

import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
import com.natiqhaciyef.common.model.ProscanSectionModel
import com.natiqhaciyef.core.base.mock.BaseMockGenerator

class GetProscanSectionsMockGenerator(
    override var takenRequest: Unit
) : BaseMockGenerator<Unit, List<ProscanSectionModel>>() {
    override var createdMock: List<ProscanSectionModel> =
        AppDetailsMockManager.getProscanSections()

    override fun getMock(
        action: ((Unit) -> List<ProscanSectionModel>?)?
    ): List<ProscanSectionModel> {
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