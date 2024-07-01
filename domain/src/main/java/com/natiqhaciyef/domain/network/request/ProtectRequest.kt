package com.natiqhaciyef.domain.network.request

import com.natiqhaciyef.domain.network.response.MaterialResponse


data class ProtectRequest(
    val material: MaterialResponse,
    val key: String
)