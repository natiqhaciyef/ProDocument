package com.natiqhaciyef.data.mock.materials

import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.common.model.Quality
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.request.CompressRequest
import com.natiqhaciyef.data.network.response.MaterialResponse

class CompressMaterialMockGenerator(
    override var takenRequest: CompressRequest
) : BaseMockGenerator<CompressRequest, MaterialResponse>() {
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
            )
        )


    override fun getMock(
        request: CompressRequest,
        action: (CompressRequest) -> MaterialResponse?
    ): MaterialResponse {
        return if (request.quality == takenRequest.quality)
            createdMock
        else
            action.invoke(request) ?: throw Companion.MockRequestException()
    }

    companion object CompressMaterialMockGenerator{
        val customRequest = CompressRequest(
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
            quality = Quality.STANDARD.name
        )
    }
}