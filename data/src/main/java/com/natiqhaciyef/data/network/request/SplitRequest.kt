package com.natiqhaciyef.data.network.request

import com.natiqhaciyef.data.network.response.MaterialResponse

data class SplitRequest(
    val title: String,
    val material: MaterialResponse,
    val firstLine: String,
    val lastLine: String
)