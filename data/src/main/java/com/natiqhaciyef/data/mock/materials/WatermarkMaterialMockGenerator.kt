package com.natiqhaciyef.data.mock.materials

import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
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
        MaterialMockManager.watermarkMaterial(takenRequest, true)

    override fun getMock(
        action: ((WatermarkRequest) -> MaterialResponse?)?
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