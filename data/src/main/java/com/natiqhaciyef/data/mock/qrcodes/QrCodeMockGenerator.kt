package com.natiqhaciyef.data.mock.qrcodes

import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.response.QrCodeResponse

class QrCodeMockGenerator(
    override var takenRequest: String
) : BaseMockGenerator<String, QrCodeResponse>() {
    override var createdMock: QrCodeResponse =
        QrCodeResponse(
            url = "url",
            result = CRUDResponse(
                resultCode = 299,
                message = "Mock qr code"
            )
        )

    override fun getMock(request: String, action: (String) -> QrCodeResponse?): QrCodeResponse {
        return if (request == takenRequest) {
            createdMock
        } else {
            action.invoke(request) ?: throw Companion.MockRequestException()
        }
    }
}