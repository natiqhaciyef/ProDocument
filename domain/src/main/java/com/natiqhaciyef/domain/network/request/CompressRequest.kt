package com.natiqhaciyef.domain.network.request

import com.natiqhaciyef.domain.network.response.MaterialResponse


data class CompressRequest(
    var material: MaterialResponse,
    var quality: String
)
