package com.natiqhaciyef.data.mock.materials

import com.natiqhaciyef.common.model.Quality
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.request.CompressRequest
import com.natiqhaciyef.data.network.response.MaterialResponse

class CompressMaterialMockGenerator(
    override var takenRequest: CompressRequest
) : BaseMockGenerator<CompressRequest, MaterialResponse>() {
    override var createdMock: MaterialResponse =
        MaterialMockManager.compressMaterial(takenRequest.quality, takenRequest.material)


    override fun getMock(
        request: CompressRequest,
        action: (CompressRequest) -> MaterialResponse?
    ): MaterialResponse {
        if (request.quality == takenRequest.quality)
            return createdMock

        return MaterialMockManager.compressMaterial(takenRequest.quality, takenRequest.material, true)
    }

    companion object CompressMaterialMockGenerator{
        val customRequest = CompressRequest(
            material = MaterialMockManager.getEmptyMaterial(),
            quality = Quality.STANDARD.name
        )
    }
}