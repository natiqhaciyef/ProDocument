package com.natiqhaciyef.data.mock.materials

import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.common.constants.MATERIAL_ID_MOCK_KEY
import com.natiqhaciyef.common.constants.MATERIAL_TOKEN_MOCK_KEY
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.NetworkConfig
import com.natiqhaciyef.data.network.response.MaterialResponse

class GetMaterialByIdMockGenerator(
    override var takenRequest: String
) : BaseMockGenerator<String, MaterialResponse>() {

    override var createdMock: MaterialResponse =
        MaterialMockManager.getMaterialById(MATERIAL_ID_MOCK_KEY)

    override fun getMock(
        request: String,
        action: (String) -> MaterialResponse?
    ): MaterialResponse {
        if (request == takenRequest)
            return createdMock

        return MaterialMockManager.getMaterialById(takenRequest)
    }
}