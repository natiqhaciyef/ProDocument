package com.natiqhaciyef.data.mock.materials

import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.data.network.request.MergeRequest
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
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
            resultCode = TWO_HUNDRED_NINETY_NINE,
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
            id = EMPTY_STRING,
            publishDate = "publishDate",
            image = EMPTY_STRING,
            title = EMPTY_STRING,
            description = EMPTY_STRING,
            type = EMPTY_STRING,
            url = EMPTY_STRING,
            result = null
        )
        val customRequest = MergeRequest(
            title = EMPTY_STRING,
            list = listOf(materialResponse, materialResponse)
        )
    }
}