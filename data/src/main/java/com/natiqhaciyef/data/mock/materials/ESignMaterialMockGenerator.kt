package com.natiqhaciyef.data.mock.materials

import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.request.ESignRequest
import com.natiqhaciyef.domain.network.response.MaterialResponse

class ESignMaterialMockGenerator(
    override var takenRequest: ESignRequest
) : BaseMockGenerator<ESignRequest, MaterialResponse>() {
    override var createdMock: MaterialResponse =
        MaterialMockManager.eSignMaterial(NOT_SIGNED, takenRequest.material)

    override fun getMock(
        action: ((ESignRequest) -> MaterialResponse?)?
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

    companion object ESignMaterialMockGenerator{
        private const val NOT_SIGNED = "not-signed"
        private const val CONVERTED_BITMAP = "converted-bitmap"

        val customRequest = ESignRequest(
            material = MaterialMockManager.getEmptyMaterial(),
            sign = NOT_SIGNED,
            signBitmap = CONVERTED_BITMAP,
            page = ZERO,
            positionX = ZERO.toFloat(),
            positionY = ZERO.toFloat()
        )
    }
}