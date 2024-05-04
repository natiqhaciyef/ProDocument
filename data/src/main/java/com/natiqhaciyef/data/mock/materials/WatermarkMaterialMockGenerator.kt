package com.natiqhaciyef.data.mock.materials

import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.data.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.request.WatermarkRequest
import com.natiqhaciyef.data.network.response.CRUDResponse
import com.natiqhaciyef.data.network.response.MaterialResponse

class WatermarkMaterialMockGenerator(
    override var takenRequest: WatermarkRequest
) : BaseMockGenerator<WatermarkRequest, MaterialResponse>() {

    override var createdMock = MaterialResponse(
        id = "materialId",
        publishDate = getNow(),
        image = "image",
        title = "title",
        description = "description",
        type = "type",
        url = "url",
        result = CRUDResponse(
            resultCode = 299,
            message = "Mock crud"
        )
    )

    override fun getMock(
        request: WatermarkRequest,
        action: (WatermarkRequest) -> MaterialResponse?
    ): MaterialResponse {
        return if (request.watermark == takenRequest.watermark){
            createdMock
        }else{
            action.invoke(request) ?: throw Companion.MockRequestException()
        }
    }

    companion object WatermarkMaterialMockGenerator{
        val customRequest = WatermarkRequest(
            title = "watermark",
            material = MaterialResponse(
                id = "materialId",
                publishDate = getNow(),
                image = "image",
                title = "title",
                description = "description",
                type = "type",
                url = "url",
                result = CRUDResponse(
                    resultCode = 299,
                    message = "Mock crud"
                )
            ),
            watermark = "watermark"
        )
    }
}