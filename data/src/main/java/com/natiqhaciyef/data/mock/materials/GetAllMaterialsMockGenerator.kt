package com.natiqhaciyef.data.mock.materials


import android.annotation.SuppressLint
import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.response.ListMaterialResponse
import com.natiqhaciyef.domain.network.response.MaterialResponse

class GetAllMaterialsMockGenerator(override var takenRequest: Unit) :
    BaseMockGenerator<Unit, ListMaterialResponse>() {

    override var createdMock: ListMaterialResponse =
        MaterialMockManager.getAllMaterials()

    override fun getMock(
        action: ((Unit) -> ListMaterialResponse?)?
    ): ListMaterialResponse {
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