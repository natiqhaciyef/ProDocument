package com.natiqhaciyef.domain.repository

import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.data.network.response.QrCodeResponse
import com.natiqhaciyef.domain.base.repository.BaseRepository
import retrofit2.Response

interface QrCodeRepository: BaseRepository {

    suspend fun readQrCodeResult(qrCode: String): NetworkResult<QrCodeResponse>
}