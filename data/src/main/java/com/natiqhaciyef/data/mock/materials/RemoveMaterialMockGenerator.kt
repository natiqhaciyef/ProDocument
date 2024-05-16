package com.natiqhaciyef.data.mock.materials

import com.natiqhaciyef.common.objects.MATERIAL_ID_MOCK_KEY
import com.natiqhaciyef.common.objects.MATERIAL_MOCK_KEY
import com.natiqhaciyef.common.objects.MATERIAL_TOKEN_MOCK_KEY
import com.natiqhaciyef.common.objects.USER_EMAIL_MOCK_KEY
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.NetworkConfig

class RemoveMaterialMockGenerator(
    override var takenRequest: Map<String, String>
) : BaseMockGenerator<Map<String, String>, CRUDResponse>() {
    override var createdMock: CRUDResponse = CRUDResponse(
        resultCode = 299,
        message = "Mock crud"
    )

    override fun getMock(
        request: Map<String, String>,
        action: (Map<String, String>) -> CRUDResponse?
    ): CRUDResponse {
        val materialId = request[MATERIAL_ID_MOCK_KEY]
        val materialToken = request[MATERIAL_TOKEN_MOCK_KEY]

        return if (
            materialId == takenRequest[MATERIAL_ID_MOCK_KEY]
            && materialToken == takenRequest[MATERIAL_TOKEN_MOCK_KEY]
        ) {
            createdMock
        } else {
            action.invoke(request) ?: throw Companion.MockRequestException()
        }
    }

    companion object RemoveMaterialMockGenerator {
        val customRequest = mapOf(
            MATERIAL_ID_MOCK_KEY to MATERIAL_ID_MOCK_KEY,
            MATERIAL_TOKEN_MOCK_KEY to NetworkConfig.HEADER_AUTHORIZATION_TYPE + MATERIAL_TOKEN_MOCK_KEY
        )
    }
}