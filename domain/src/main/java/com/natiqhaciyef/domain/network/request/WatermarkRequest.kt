package com.natiqhaciyef.domain.network.request

import com.natiqhaciyef.domain.network.response.MaterialResponse


data class WatermarkRequest(
    val title: String,
    val material: MaterialResponse,
    val watermark: String
)