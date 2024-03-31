package com.natiqhaciyef.data.source

import com.natiqhaciyef.data.network.handleNetworkResponse
import com.natiqhaciyef.data.network.response.QrCodeResponse
import com.natiqhaciyef.data.network.service.QrCodeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QrCodeDataSource(
    private val service: QrCodeService
){

    suspend fun readQrCodeResult(qrCode: String) = withContext(Dispatchers.IO){
        handleNetworkResponse { service.readQrCodeResult(qrCode) }
    }
}