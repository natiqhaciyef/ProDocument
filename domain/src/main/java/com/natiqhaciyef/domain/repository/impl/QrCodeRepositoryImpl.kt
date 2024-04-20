package com.natiqhaciyef.domain.repository.impl

import android.net.Uri
import com.natiqhaciyef.data.network.response.QrCodeResponse
import com.natiqhaciyef.data.source.QrCodeDataSource
import com.natiqhaciyef.domain.repository.QrCodeRepository

class QrCodeRepositoryImpl(private val ds: QrCodeDataSource): QrCodeRepository {
    override suspend fun readQrCodeResult(qrCode: String) =
        ds.readQrCodeResult(qrCode)

}