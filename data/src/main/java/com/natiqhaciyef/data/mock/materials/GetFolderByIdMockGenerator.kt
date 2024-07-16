package com.natiqhaciyef.data.mock.materials

import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.response.FolderResponse

class GetFolderByIdMockGenerator(
    override var takenRequest: String
) : BaseMockGenerator<String, FolderResponse>() {
    override var createdMock: FolderResponse =
        MaterialMockManager.getFolderById(takenRequest)

    override fun getMock(action: ((String) -> FolderResponse?)?): FolderResponse {
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