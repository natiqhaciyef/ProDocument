package com.natiqhaciyef.data.mock.materials

import androidx.core.net.toUri
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
import com.natiqhaciyef.common.constants.TWO
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.request.ProtectRequest
import com.natiqhaciyef.domain.network.response.MaterialResponse

class ProtectMaterialMockGenerator(
    override var takenRequest: ProtectRequest
) : BaseMockGenerator<ProtectRequest, MaterialResponse>() {
    override var createdMock: MaterialResponse =
        MaterialMockManager.protectMaterial(takenRequest.key, takenRequest.material, true)

    override fun getMock(
        action: ((ProtectRequest) -> MaterialResponse?)?
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

    companion object ProtectMaterialMockGenerator{
        private const val PROTECTED = "Protected"
        val customRequest = ProtectRequest(
            material = MaterialMockManager.getEmptyMaterial(),
            key = PROTECTED
        )
    }
}