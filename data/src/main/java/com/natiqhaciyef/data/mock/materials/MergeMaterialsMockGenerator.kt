package com.natiqhaciyef.data.mock.materials

import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.data.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.request.MergeRequest
import com.natiqhaciyef.data.network.response.CRUDResponse
import com.natiqhaciyef.data.network.response.MaterialResponse

class MergeMaterialsMockGenerator(
    override var takenRequest: MergeRequest
) : BaseMockGenerator<MergeRequest, MaterialResponse>() {
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
            message = "Mock crud"
        )
    )

    override fun getMock(
        request: MergeRequest,
        action: (MergeRequest) -> MaterialResponse?
    ): MaterialResponse {
        return if (request == takenRequest) {
            createdMock
        } else {
            action.invoke(request) ?: throw Companion.MockRequestException()
        }
    }

    companion object MergeMaterialsMockGenerator {
        private var materialResponse = MaterialResponse(
            id = "",
            publishDate = "publishDate",
            image = "",
            title = "",
            description = "",
            type = "",
            url = "",
            result = null
        )
        val customRequest = MergeRequest(
            title = "",
            list = listOf(materialResponse, materialResponse)
        )
    }
}