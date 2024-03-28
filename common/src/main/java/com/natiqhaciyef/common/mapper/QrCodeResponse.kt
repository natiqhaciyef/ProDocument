package com.natiqhaciyef.common.mapper

import com.natiqhaciyef.common.model.mapped.MappedQrCodeResultModel
import com.natiqhaciyef.data.network.response.QrCodeResponse


fun QrCodeResponse.toMapped(): MappedQrCodeResultModel{
    return MappedQrCodeResultModel(
        url = this.url,
        result = this.result?.toModel()
    )
}

fun MappedQrCodeResultModel.toMapped(): QrCodeResponse{
    return QrCodeResponse(
        url = this.url,
        result = this.result?.toResponse()
    )
}