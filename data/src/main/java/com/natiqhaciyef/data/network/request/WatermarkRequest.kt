package com.natiqhaciyef.data.network.request

import com.natiqhaciyef.data.network.response.MaterialResponse

data class WatermarkRequest(
    val title: String,
    val material: MaterialResponse,
    val watermark: String
)