package com.natiqhaciyef.data.mock.materials

import com.natiqhaciyef.common.constants.MATERIAL_ID_MOCK_KEY
import com.natiqhaciyef.common.constants.MATERIAL_TOKEN_MOCK_KEY
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.NetworkConfig

class RemoveMaterialMockGenerator(
    override var takenRequest: String
) : BaseMockGenerator<String, CRUDResponse>() {
    override var createdMock: CRUDResponse =
        MaterialMockManager.getCrudResult(REMOVE)

    override fun getMock(
        request: String,
        action: (String) -> CRUDResponse?
    ): CRUDResponse {
        if (takenRequest == request)
            return createdMock

        return MaterialMockManager.removeMaterial(takenRequest, REMOVE)
    }

    companion object RemoveMaterialMockGenerator {
        private const val REMOVE = "Material removed"
    }
}