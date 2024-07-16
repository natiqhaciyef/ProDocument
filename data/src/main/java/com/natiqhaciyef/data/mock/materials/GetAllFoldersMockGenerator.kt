package com.natiqhaciyef.data.mock.materials

import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.response.FolderResponse

class GetAllFoldersMockGenerator(
    override var takenRequest: Unit
) : BaseMockGenerator<Unit, List<FolderResponse>>() {
    override var createdMock: List<FolderResponse> =
        MaterialMockManager.getAllFolders()

    override fun getMock(action: ((Unit) -> List<FolderResponse>?)?): List<FolderResponse> {
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