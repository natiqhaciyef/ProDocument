package com.natiqhaciyef.domain.network.request

import com.natiqhaciyef.domain.network.response.MaterialResponse


data class MergeRequest(
    val title: String,
    val list: List<MaterialResponse>
)