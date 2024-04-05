package com.natiqhaciyef.common.mapper

import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.data.network.response.TokenResponse

fun MappedTokenModel.toResponse(): TokenResponse {
    return TokenResponse(
        uid = this.uid,
        result = this.result,
        premiumToken = this.premiumToken,
        premiumTokenExpiryDate = this.premiumTokenExpiryDate
    )
}

fun TokenResponse.toMapped(): MappedTokenModel {
    return MappedTokenModel(
        uid = uid,
        result = result,
        premiumToken = this.premiumToken,
        premiumTokenExpiryDate = this.premiumTokenExpiryDate,
        securityDeviceCode = null
    )
}