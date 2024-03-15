package com.natiqhaciyef.common.mapper

import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.data.network.response.TokenResponse

fun MappedTokenModel.toResponse(): TokenResponse {
    return TokenResponse(
        uid = uid,
        result = result
    )
}

fun TokenResponse.toMapped(): MappedTokenModel {
    return MappedTokenModel(
        uid = uid,
        result = result
    )
}