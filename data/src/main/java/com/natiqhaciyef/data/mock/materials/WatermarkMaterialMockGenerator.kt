package com.natiqhaciyef.data.mock.materials

import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.domain.network.request.WatermarkRequest
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.response.MaterialResponse

class WatermarkMaterialMockGenerator(
    override var takenRequest: WatermarkRequest
) : BaseMockGenerator<WatermarkRequest, MaterialResponse>() {

    override var createdMock =
        MaterialMockManager.watermarkMaterial(takenRequest)

    override fun getMock(
        request: WatermarkRequest,
        action: (WatermarkRequest) -> MaterialResponse?
    ): MaterialResponse {
        if (request == takenRequest)
            return createdMock

        return MaterialMockManager.watermarkMaterial(takenRequest, true)
    }

    companion object WatermarkMaterialMockGenerator{
        private const val TITLE = "title"
        private const val WATERMARK = "watermark"

        val customRequest = WatermarkRequest(
            title = TITLE,
            material = MaterialMockManager.getEmptyMaterial(),
            watermark = WATERMARK
        )
    }
}