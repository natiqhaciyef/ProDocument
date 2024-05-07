package com.natiqhaciyef.data.mapper

import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.data.network.response.TokenResponse

fun MappedTokenModel.toResponse(): TokenResponse {
    return TokenResponse(
        accessToken = this.accessToken,
        result = this.result?.toResponse(),
        refreshToken = this.refreshToken,
    )
}

fun TokenResponse.toMapped(): MappedTokenModel {
    return MappedTokenModel(
        accessToken = accessToken,
        result = result?.toModel(),
        refreshToken = this.refreshToken,
    )
}