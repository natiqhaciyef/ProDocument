package com.natiqhaciyef.data.mock.materials


import android.annotation.SuppressLint
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.response.ListMaterialResponse
import com.natiqhaciyef.domain.network.response.MaterialResponse

class GetAllMaterialsMockGenerator(override var takenRequest: Unit) :
    BaseMockGenerator<Unit, ListMaterialResponse>() {

    override var createdMock: ListMaterialResponse =
        MaterialMockManager.getAllMaterials()

    override fun getMock(
        request: Unit,
        action: (Unit) -> ListMaterialResponse?
    ): ListMaterialResponse {
        return createdMock
    }
}