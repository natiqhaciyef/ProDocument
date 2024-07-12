package com.natiqhaciyef.data.mock.materials

import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.domain.network.request.MergeRequest
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.response.MaterialResponse

class MergeMaterialsMockGenerator(
    override var takenRequest: MergeRequest
) : BaseMockGenerator<MergeRequest, MaterialResponse>() {
    override var createdMock: MaterialResponse =
        MaterialMockManager.mergeMaterial(takenRequest, true)

    override fun getMock(
        action: ((MergeRequest) -> MaterialResponse?)?
    ): MaterialResponse {
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

    companion object MergeMaterialsMockGenerator {
        private val materialResponse = MaterialMockManager.getEmptyMaterial()
        val customRequest = MergeRequest(
            title = EMPTY_STRING,
            list = listOf(materialResponse, materialResponse)
        )
    }
}