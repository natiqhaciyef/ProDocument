package com.natiqhaciyef.domain.mapper

import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.data.network.response.TokenResponse

fun MappedTokenModel.toResponse(): TokenResponse {
    return TokenResponse(
        uid = this.uid,
        result = this.result?.toResponse(),
        email = this.email,
    )
}

fun TokenResponse.toMapped(): MappedTokenModel {
    return MappedTokenModel(
        uid = uid,
        result = result?.toModel(),
        email = this.email,
    )
}