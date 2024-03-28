package com.natiqhaciyef.domain.repository

import com.natiqhaciyef.data.network.response.QrCodeResponse
import com.natiqhaciyef.domain.base.BaseRepository

interface QrCodeRepository: BaseRepository{

    suspend fun readQrCodeResult(qrCode: String): QrCodeResponse?
}