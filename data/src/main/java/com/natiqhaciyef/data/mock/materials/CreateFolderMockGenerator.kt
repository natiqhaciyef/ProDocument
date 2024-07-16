package com.natiqhaciyef.data.mock.materials

import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.request.FolderRequest

class CreateFolderMockGenerator(
    override var takenRequest: FolderRequest
) : BaseMockGenerator<FolderRequest, CRUDResponse>() {
    override var createdMock: CRUDResponse =
        MaterialMockManager.createFolder(takenRequest, CREATE_MATERIAL)

    override fun getMock(action: ((FolderRequest) -> CRUDResponse?)?): CRUDResponse {
        if (action != null)
            try {
                return action.invoke(takenRequest) ?: throw Companion
                    .MockRequestException(MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN)
            } catch (e: Exception) {
                println(e)
            }

        return createdMock
    }

    companion object CreateFolderMockGenerator{
        private const val CREATE_MATERIAL = "Material created"
    }
}