package com.natiqhaciyef.data.mock.materials

import com.natiqhaciyef.common.constants.MATERIAL_MOCK_KEY
import com.natiqhaciyef.common.constants.MATERIAL_TOKEN_MOCK_KEY
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.NetworkConfig
import com.natiqhaciyef.data.network.response.MaterialResponse

class CreateMaterialMockGenerator(
    override var takenRequest: Map<String, Any>
) : BaseMockGenerator<Map<String, Any>, CRUDResponse>() {
    override var createdMock: CRUDResponse = CRUDResponse(
        resultCode = TWO_HUNDRED_NINETY_NINE,
        message = "Mock crud"
    )

    override fun getMock(
        request: Map<String, Any>,
        action: (Map<String, Any>) -> CRUDResponse?
    ): CRUDResponse {
        val material = request[MATERIAL_TOKEN_MOCK_KEY]
        val model = request[MATERIAL_MOCK_KEY]

        return if (
            material == takenRequest[MATERIAL_TOKEN_MOCK_KEY]
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
            MATERIAL_TOKEN_MOCK_KEY to NetworkConfig.HEADER_AUTHORIZATION_TYPE + MATERIAL_TOKEN_MOCK_KEY
        )
    }
}