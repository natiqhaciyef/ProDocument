package com.natiqhaciyef.data.mock.materials



import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.response.MaterialResponse

class GetAllMaterialsMockGenerator(override var takenRequest: Unit) :
    BaseMockGenerator<Unit, List<MaterialResponse>>() {

    override var createdMock: List<MaterialResponse> =
        MaterialMockManager.getAllMaterials()

    override fun getMock(
        action: ((Unit) -> List<MaterialResponse>?)?
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
}