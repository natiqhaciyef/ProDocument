package com.natiqhaciyef.data.mock.materials

import com.natiqhaciyef.common.constants.MATERIAL_MOCK_KEY
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.response.MaterialResponse

class CreateMaterialMockGenerator(
    override var takenRequest: MaterialResponse
) : BaseMockGenerator<MaterialResponse, CRUDResponse>() {
    override var createdMock: CRUDResponse =
        MaterialMockManager.getCrudResult(CREATE_MATERIAL)

    override fun getMock(
        request: MaterialResponse,
        action: (MaterialResponse) -> CRUDResponse?
    ): CRUDResponse {
        if (takenRequest == request)
            return createdMock

        return MaterialMockManager.createMaterial(takenRequest, CREATE_MATERIAL)
    }

    companion object CreateMaterialMockGenerator {
        private const val CREATE_MATERIAL = "create material"
        val customRequest = MaterialMockManager
            .getEmptyMaterial()
    }
}