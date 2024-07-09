package com.natiqhaciyef.data.mock.materials

import com.natiqhaciyef.common.constants.MATERIAL_MOCK_KEY
import com.natiqhaciyef.common.constants.MATERIAL_TOKEN_MOCK_KEY
import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.NetworkConfig
import com.natiqhaciyef.domain.network.response.MaterialResponse

class UpdateMaterialMockGenerator(
    override var takenRequest: MaterialResponse
) : BaseMockGenerator<MaterialResponse, CRUDResponse>() {
    override var createdMock: CRUDResponse =
        MaterialMockManager.updateMaterial(customMaterial = takenRequest, UPDATE)

    override fun getMock(
        action: ((MaterialResponse) -> CRUDResponse?)?
    ): CRUDResponse {
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

    companion object UpdateMaterialMockGenerator {
        private const val UPDATE = "Material updated"
        val customRequest = MaterialMockManager.getEmptyMaterial()
    }
}