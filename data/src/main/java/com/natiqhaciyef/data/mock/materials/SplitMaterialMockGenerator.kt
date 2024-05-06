package com.natiqhaciyef.data.mock.materials

import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.data.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.request.SplitRequest
import com.natiqhaciyef.data.network.response.CRUDResponse
import com.natiqhaciyef.data.network.response.MaterialResponse

class SplitMaterialMockGenerator(
    override var takenRequest: SplitRequest
) : BaseMockGenerator<SplitRequest, List<MaterialResponse>>(){

    override var createdMock: List<MaterialResponse> = listOf(
        MaterialResponse(
            id = "materialId - 1",
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
        MaterialResponse(
            id = "materialId - 2",
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
    )

    override fun getMock(
        request: SplitRequest,
        action: (SplitRequest) -> List<MaterialResponse>?
    ): List<MaterialResponse> =
        if (request == takenRequest){
            createdMock
        }else{
            action.invoke(request) ?: throw Companion.MockRequestException()
        }

    companion object SplitMaterialMockGenerator{
        val customRequest = SplitRequest(
            title = "title",
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
            firstLine = "first",
            lastLine = "last"
        )
    }
}