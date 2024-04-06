package com.natiqhaciyef.domain.mapper

import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.data.network.response.TokenResponse

fun MappedTokenModel.toResponse(): TokenResponse {
    return TokenResponse(
        uid = this.uid,
        result = this.result?.toResponse(),
        premiumToken = this.premiumToken,
        premiumTokenExpiryDate = this.premiumTokenExpiryDate
    )
}

fun TokenResponse.toMapped(): MappedTokenModel {
    return MappedTokenModel(
        uid = uid,
        result = result?.toModel(),
        premiumToken = this.premiumToken,
        premiumTokenExpiryDate = this.premiumTokenExpiryDate,
        securityDeviceCode = null
    )
}