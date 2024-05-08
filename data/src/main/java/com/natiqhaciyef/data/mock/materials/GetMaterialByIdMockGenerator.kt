package com.natiqhaciyef.data.mock.materials

import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.common.objects.MATERIAL_ID_MOCK_KEY
import com.natiqhaciyef.common.objects.USER_EMAIL_MOCK_KEY
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.response.MaterialResponse

class GetMaterialByIdMockGenerator(
    override var takenRequest: Map<String, String>
) : BaseMockGenerator<Map<String, String>, MaterialResponse>() {

    override var createdMock: MaterialResponse = MaterialResponse(
        id = "materialId",
        publishDate = getNow(),
        image = "image",
        title = "title",
        description = "description",
        type = "type",
        url = "url",
        result = CRUDResponse(
            resultCode = 299,
            message = "Mock material"
        )
    )

    override fun getMock(
        request: Map<String, String>,
        action: (Map<String, String>) -> MaterialResponse?
    ): MaterialResponse {
        val materialId = request[MATERIAL_ID_MOCK_KEY]
        val email = request[USER_EMAIL_MOCK_KEY]

        return if (
            materialId == takenRequest[MATERIAL_ID_MOCK_KEY]
            && email == takenRequest[USER_EMAIL_MOCK_KEY]
        ) {
            createdMock
        } else {
            action.invoke(request) ?: throw Companion.MockRequestException()
        }
    }

    companion object GetMaterialByIdMockGenerator {
        val customRequest = mapOf(
            USER_EMAIL_MOCK_KEY to USER_EMAIL_MOCK_KEY,
            MATERIAL_ID_MOCK_KEY to MATERIAL_ID_MOCK_KEY
        )
    }
}