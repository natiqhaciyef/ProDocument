package com.natiqhaciyef.data.mock.materials

import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.response.MaterialResponse

class GetMaterialsByFolderIdMockGenerator(
    override var takenRequest: String
) : BaseMockGenerator<String, List<MaterialResponse>>() {
    override var createdMock: List<MaterialResponse> =
        MaterialMockManager.getMaterialsByFolderId(takenRequest)

    override fun getMock(action: ((String) -> List<MaterialResponse>?)?): List<MaterialResponse> {
        if (action != null)
            try {
                return action.invoke(takenRequest) ?: throw Companion
                    .MockRequestException(MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN)
            } catch (e: Exception) {
                println(e)
            }

        return createdMock
    }
}