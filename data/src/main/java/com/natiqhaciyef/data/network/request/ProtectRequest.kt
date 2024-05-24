package com.natiqhaciyef.data.network.request

import com.natiqhaciyef.data.network.response.MaterialResponse

data class ProtectRequest(
    val material: MaterialResponse,
    val key: String
)