package com.natiqhaciyef.data.network.request

import com.natiqhaciyef.data.network.response.MaterialResponse

data class ESignRequest(
    val sign: String,
    val material: MaterialResponse
)
