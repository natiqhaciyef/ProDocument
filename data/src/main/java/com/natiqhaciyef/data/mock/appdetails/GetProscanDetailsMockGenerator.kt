package com.natiqhaciyef.data.mock.appdetails

import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
import com.natiqhaciyef.common.model.ProScanInfoModel
import com.natiqhaciyef.core.base.mock.BaseMockGenerator

class GetProscanDetailsMockGenerator(
    override var takenRequest: Unit
) : BaseMockGenerator<Unit, ProScanInfoModel>() {
    override var createdMock: ProScanInfoModel =
        AppDetailsMockManager.getProscanDetails()

    override fun getMock(
        action: ((Unit) -> ProScanInfoModel?)?
    ): ProScanInfoModel {
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