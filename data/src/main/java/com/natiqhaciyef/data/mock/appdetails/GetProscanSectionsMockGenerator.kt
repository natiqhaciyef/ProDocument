package com.natiqhaciyef.data.mock.appdetails

import com.natiqhaciyef.common.model.ProscanSectionModel
import com.natiqhaciyef.core.base.mock.BaseMockGenerator

class GetProscanSectionsMockGenerator(
    override var takenRequest: Unit
) : BaseMockGenerator<Unit, List<ProscanSectionModel>>() {
    override var createdMock: List<ProscanSectionModel> =
        AppDetailsMockManager.getProscanSections()

    override fun getMock(
        request: Unit,
        action: (Unit) -> List<ProscanSectionModel>?
    ): List<ProscanSectionModel> {
        return createdMock
    }
}