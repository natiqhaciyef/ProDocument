package com.natiqhaciyef.data.mock.users

import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.response.GraphDetailModel
import com.natiqhaciyef.domain.network.response.GraphDetailsListResponse

class GetUserStatisticsMockGenerator(
    override var takenRequest: Unit
) : BaseMockGenerator<Unit, GraphDetailsListResponse>() {
    override var createdMock: GraphDetailsListResponse =
        GraphDetailsListResponse(
            details = listOf(
                GraphDetailModel(
                    title = "Watermark",
                    type = "watermark",
                    percentage = 12.0,
                    backgroundColor = "brown"
                ),
                GraphDetailModel(
                    title = "Scan",
                    type = "scan",
                    percentage = 60.0,
                    backgroundColor = "orange"
                ),
                GraphDetailModel(
                    title = "Protect",
                    type = "protect",
                    percentage = 28.0,
                    backgroundColor = "green"
                )
            )
        )

    override fun getMock(
        request: Unit,
        action: (Unit) -> GraphDetailsListResponse?
    ): GraphDetailsListResponse {
        return createdMock
    }
}