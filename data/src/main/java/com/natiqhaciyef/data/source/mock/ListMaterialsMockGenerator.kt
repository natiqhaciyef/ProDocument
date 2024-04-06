package com.natiqhaciyef.data.source.mock


import android.annotation.SuppressLint
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.data.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.response.CRUDResponse
import com.natiqhaciyef.data.network.response.ListMaterialResponse
import com.natiqhaciyef.data.network.response.MaterialResponse

class ListMaterialsMockGenerator(override var takenRequest: String) :
    BaseMockGenerator<String, ListMaterialResponse>() {

    @SuppressLint("NewApi")
    override var createdMock: ListMaterialResponse = ListMaterialResponse(
        materials = listOf(
            MaterialResponse(
                id = "id material",
                publishDate = getNow(),
                image = "image",
                title = "title",
                description = "description",
                type = "type",
                url = "url",
                result = CRUDResponse(
                    resultCode = -2,
                    message = "mock material"
                )
            )
        ),
        id = "id",
        result = CRUDResponse(
            resultCode = -1,
            message = "Mock"
        ),
        publishDate = getNow()
    )

    override fun createInstance(): BaseMockGenerator<String, ListMaterialResponse> {
        return this
    }
}