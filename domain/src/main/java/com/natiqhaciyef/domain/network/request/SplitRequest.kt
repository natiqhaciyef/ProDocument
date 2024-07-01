package com.natiqhaciyef.domain.network.request

import com.natiqhaciyef.domain.network.response.MaterialResponse


data class SplitRequest(
    val title: String,
    val material: MaterialResponse,
    val firstLine: String,
    val lastLine: String
)