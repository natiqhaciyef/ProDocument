package com.natiqhaciyef.data.mock.materials

import com.natiqhaciyef.common.constants.EMPTY_STRING
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
        request: MergeRequest,
        action: (MergeRequest) -> MaterialResponse?
    ): MaterialResponse {
        if (request == takenRequest)
            return createdMock

        return MaterialMockManager.mergeMaterial(takenRequest, true)
    }

    companion object MergeMaterialsMockGenerator {
        private val materialResponse = MaterialMockManager.getEmptyMaterial()
        val customRequest = MergeRequest(
            title = EMPTY_STRING,
            list = listOf(materialResponse, materialResponse)
        )
    }
}