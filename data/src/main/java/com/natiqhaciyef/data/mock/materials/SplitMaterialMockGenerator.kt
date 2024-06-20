package com.natiqhaciyef.data.mock.materials

import com.natiqhaciyef.common.constants.TWO_HUNDRED
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.data.network.request.SplitRequest
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
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
                resultCode = TWO_HUNDRED_NINETY_NINE,
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
                resultCode = TWO_HUNDRED_NINETY_NINE,
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
                    resultCode = TWO_HUNDRED_NINETY_NINE,
                    message = "Mock crud"
                )
            ),
            firstLine = "first",
            lastLine = "last"
        )
    }
}