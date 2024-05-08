package com.natiqhaciyef.data.mock.materials

import com.natiqhaciyef.common.objects.MATERIAL_MOCK_KEY
import com.natiqhaciyef.common.objects.USER_EMAIL_MOCK_KEY
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.response.MaterialResponse

class CreateMaterialMockGenerator(
    override var takenRequest: Map<String, Any>
) : BaseMockGenerator<Map<String, Any>, CRUDResponse>() {
    override var createdMock: CRUDResponse = CRUDResponse(
        resultCode = 299,
        message = "Mock crud"
    )

    override fun getMock(
        request: Map<String, Any>,
        action: (Map<String, Any>) -> CRUDResponse?
    ): CRUDResponse {
        val material = request[USER_EMAIL_MOCK_KEY]
        val model = request[MATERIAL_MOCK_KEY]

        return if (
            material == takenRequest[USER_EMAIL_MOCK_KEY]
            && model == takenRequest[MATERIAL_MOCK_KEY]
        ){
            createdMock
        }else{
            action.invoke(request) ?: throw Companion.MockRequestException()
        }
    }

    companion object CreateMaterialMockGenerator {
        private val materialResponse = MaterialResponse(
            id = "createId",
            publishDate = "publishDate",
            image = "image",
            title = "title",
            description = "description",
            type = "type",
            url = "url",
            result = null
        )

        val customRequest = mapOf(
            MATERIAL_MOCK_KEY to materialResponse,
            USER_EMAIL_MOCK_KEY to USER_EMAIL_MOCK_KEY
        )
    }
}