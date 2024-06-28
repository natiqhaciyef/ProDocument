package com.natiqhaciyef.data.mock.materials

import androidx.core.net.toUri
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.TWO
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.request.ProtectRequest
import com.natiqhaciyef.data.network.response.MaterialResponse

class ProtectMaterialMockGenerator(
    override var takenRequest: ProtectRequest
) : BaseMockGenerator<ProtectRequest, MaterialResponse>() {
    override var createdMock: MaterialResponse =
        MaterialResponse(
            id = "materialId",
            publishDate = getNow(),
            image = "image",
            title = "title",
            description = "description",
            type = "type",
            url = "url",
            result = CRUDResponse(
                resultCode = TWO_HUNDRED_NINETY_NINE,
                message = "Mock crud"
            ),
            protectionKey = "Protected"
        )

    override fun getMock(
        request: ProtectRequest,
        action: (ProtectRequest) -> MaterialResponse?
    ): MaterialResponse {
        return if (request.key == takenRequest.key && request.material.title == takenRequest.material.title)
            createdMock
        else
            action.invoke(request) ?: throw Companion.MockRequestException()
    }

    companion object ProtectMaterialMockGenerator{
        val customRequest = ProtectRequest(
            material = MaterialResponse(
                id = "materialId",
                publishDate = getNow(),
                image = "image",
                title = "title",
                description = "description",
                type = "type",
                url = EMPTY_STRING,
                result = CRUDResponse(
                    resultCode = TWO_HUNDRED_NINETY_NINE,
                    message = "Mock crud"
                )
            ),
            key = "Protected"
        )
    }
}