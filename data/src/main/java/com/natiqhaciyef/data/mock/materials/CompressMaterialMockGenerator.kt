package com.natiqhaciyef.data.mock.materials

import androidx.core.net.toUri
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.Quality
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
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
                resultCode = 299,
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
            material = MappedMaterialModel(
                id = "materialId",
                createdDate = getNow(),
                image = "image",
                title = "title",
                description = "description",
                type = "type",
                url = "url".toUri(),
                result = CRUDModel(
                    resultCode = 299,
                    message = "Mock crud"
                )
            ),
            quality = Quality.STANDARD.name
        )
    }
}