package com.natiqhaciyef.data.mock.materials

import android.graphics.Bitmap
import androidx.core.graphics.createBitmap
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.request.ESignRequest
import com.natiqhaciyef.data.network.response.MaterialResponse

class ESignMaterialMockGenerator(
    override var takenRequest: ESignRequest
) : BaseMockGenerator<ESignRequest, MaterialResponse>() {
    override var createdMock: MaterialResponse =
        MaterialResponse(
            id = "materialId",
            publishDate = getNow(),
            image = "image",
            title = "title",
            description = "description",
            type = "type",
            url = "url",
            result = CRUDResponse(
                resultCode = TWO_HUNDRED_NINETY_NINE,
                message = "Mock crud"
            ),
            protectionKey = "Protected"
        )

    override fun getMock(
        request: ESignRequest,
        action: (ESignRequest) -> MaterialResponse?
    ): MaterialResponse {
        return if (request.sign == takenRequest.sign)
            createdMock
        else
            action.invoke(request) ?: throw Companion.MockRequestException()
    }

    companion object ESignMaterialMockGenerator{
        val customRequest = ESignRequest(
            material = MaterialResponse(
                id = "materialId",
                publishDate = getNow(),
                image = "image",
                title = "title",
                description = "description",
                type = "type",
                url = EMPTY_STRING,
                result = CRUDResponse(
                    resultCode = TWO_HUNDRED_NINETY_NINE,
                    message = "Mock crud"
                )
            ),
            sign = "not-signed",
            signBitmap = "converted-bitmap",
            page = ZERO,
            positionX = ZERO.toFloat(),
            positionY = ZERO.toFloat()
        )
    }
}