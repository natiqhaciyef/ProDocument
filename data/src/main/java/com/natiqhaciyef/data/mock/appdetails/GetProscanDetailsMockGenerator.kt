package com.natiqhaciyef.data.mock.appdetails

import com.natiqhaciyef.common.model.ProScanInfoModel
import com.natiqhaciyef.core.base.mock.BaseMockGenerator

class GetProscanDetailsMockGenerator(
    override var takenRequest: Unit
) : BaseMockGenerator<Unit, ProScanInfoModel>() {
    override var createdMock: ProScanInfoModel =
        AppDetailsMockManager.getProscanDetails()

    override fun getMock(request: Unit, action: (Unit) -> ProScanInfoModel?): ProScanInfoModel {
        return createdMock
    }
}