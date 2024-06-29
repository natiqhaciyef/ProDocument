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

    override var createdMock: List<MaterialResponse> =
        MaterialMockManager.splitMaterial(takenRequest)

    override fun getMock(
        request: SplitRequest,
        action: (SplitRequest) -> List<MaterialResponse>?
    ): List<MaterialResponse> {
        if (request == takenRequest)
            return createdMock

        return MaterialMockManager.splitMaterial(takenRequest, true)
    }

    companion object SplitMaterialMockGenerator{
        private const val TITLE = "title"
        private const val FIRST_LINE = "first line"
        private const val LAST_LINE = "last line"
        val customRequest = SplitRequest(
            title = TITLE,
            material = MaterialMockManager.getEmptyMaterial(),
            firstLine = FIRST_LINE,
            lastLine = LAST_LINE
        )
    }
}