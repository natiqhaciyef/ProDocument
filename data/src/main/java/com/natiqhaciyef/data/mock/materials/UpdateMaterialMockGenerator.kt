package com.natiqhaciyef.data.mock.materials

import com.natiqhaciyef.common.constants.MATERIAL_MOCK_KEY
import com.natiqhaciyef.common.constants.MATERIAL_TOKEN_MOCK_KEY
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.NetworkConfig
import com.natiqhaciyef.domain.network.response.MaterialResponse

class UpdateMaterialMockGenerator(
    override var takenRequest: MaterialResponse
) : BaseMockGenerator<MaterialResponse, CRUDResponse>() {
    override var createdMock: CRUDResponse =
        MaterialMockManager.getCrudResult(UPDATE)

    override fun getMock(
        request: MaterialResponse,
        action: (MaterialResponse) -> CRUDResponse?
    ): CRUDResponse {
        if (takenRequest == request)
            return createdMock

        return MaterialMockManager.updateMaterial(customMaterial = takenRequest, UPDATE)
    }

    companion object UpdateMaterialMockGenerator {
        private const val UPDATE = "Material updated"
        val customRequest = MaterialMockManager.getEmptyMaterial()
    }
}