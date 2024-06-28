package com.natiqhaciyef.data.network.request

import com.natiqhaciyef.data.network.response.MaterialResponse

data class CompressRequest(
    var material: MaterialResponse,
    var quality: String
)
