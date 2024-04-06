package com.natiqhaciyef.domain.mapper

import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.data.network.response.CRUDResponse

fun CRUDResponse.toModel(): CRUDModel{
    return CRUDModel(
        resultCode = resultCode,
        message = message
    )
}
fun CRUDModel.toResponse(): CRUDResponse{
    return CRUDResponse(
        resultCode = resultCode,
        message = message
    )
}