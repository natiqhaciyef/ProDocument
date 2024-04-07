package com.natiqhaciyef.data.mock.materials


import android.annotation.SuppressLint
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.data.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.response.CRUDResponse
import com.natiqhaciyef.data.network.response.ListMaterialResponse
import com.natiqhaciyef.data.network.response.MaterialResponse

class GetAllMaterialsMockGenerator(override var takenRequest: String) :
    BaseMockGenerator<String, ListMaterialResponse>() {

    @SuppressLint("NewApi")
    override var createdMock: ListMaterialResponse = ListMaterialResponse(
        materials = listOf(
            MaterialResponse(
                id = "materialId",
                publishDate = getNow(),
                image = "image",
                title = "title",
                description = "description",
                type = "type",
                url = "url",
                result = CRUDResponse(
                    resultCode = 299,
                    message = "mock material"
                )
            )
        ),
        id = "listMaterialId",
        result = CRUDResponse(
            resultCode = 299,
            message = "Mock"
        ),
        publishDate = getNow()
    )

    override fun getMock(
        request: String,
        action: (String) -> ListMaterialResponse?
    ): ListMaterialResponse = if (request == takenRequest) {
        createdMock
    } else {
        action.invoke(request) ?: throw Companion.MockRequestException()
    }
}