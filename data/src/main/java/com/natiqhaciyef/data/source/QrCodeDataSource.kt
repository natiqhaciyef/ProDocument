package com.natiqhaciyef.data.source

import com.natiqhaciyef.common.objects.QR_CODE_MOCK_KEY
import com.natiqhaciyef.core.base.mock.generateMockerClass
import com.natiqhaciyef.data.mock.qrcodes.QrCodeMockGenerator
import com.natiqhaciyef.data.network.LoadType
import com.natiqhaciyef.data.network.handleNetworkResponse
import com.natiqhaciyef.data.network.service.QrCodeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QrCodeDataSource(
    private val service: QrCodeService
) {

    suspend fun readQrCodeResult(qrCode: String) = withContext(Dispatchers.IO) {
        val mock = generateMockerClass(QrCodeMockGenerator::class, qrCode)
            .getMock(QR_CODE_MOCK_KEY) { null }

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.readQrCodeResult(qrCode)
        }
    }
}