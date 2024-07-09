package com.natiqhaciyef.data.mock.materials

import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
import com.natiqhaciyef.common.model.Quality
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.request.CompressRequest
import com.natiqhaciyef.domain.network.response.MaterialResponse

class CompressMaterialMockGenerator(
    override var takenRequest: CompressRequest
) : BaseMockGenerator<CompressRequest, MaterialResponse>() {
    override var createdMock: MaterialResponse =
        MaterialMockManager.compressMaterial(takenRequest.quality, takenRequest.material, true)


    override fun getMock(
        action: ((CompressRequest) -> MaterialResponse?)?
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

    companion object CompressMaterialMockGenerator{
        val customRequest = CompressRequest(
            material = MaterialMockManager.getEmptyMaterial(),
            quality = Quality.STANDARD.name
        )
    }
}