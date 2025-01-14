package com.natiqhaciyef.data.mock.materials

import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
import com.natiqhaciyef.common.constants.TWO_HUNDRED
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.domain.network.request.SplitRequest
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.response.MaterialResponse

class SplitMaterialMockGenerator(
    override var takenRequest: SplitRequest
) : BaseMockGenerator<SplitRequest, List<MaterialResponse>>(){

    override var createdMock: List<MaterialResponse> =
        MaterialMockManager.splitMaterial(takenRequest, true)


    override fun getMock(
        action: ((SplitRequest) -> List<MaterialResponse>?)?
    ): List<MaterialResponse> {
        if (action != null)
            try {
                return action.invoke(takenRequest) ?: throw Companion.MockRequestException(
                    MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
                )
            } catch (e: Exception) {
                println(e)
            }

        return createdMock
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