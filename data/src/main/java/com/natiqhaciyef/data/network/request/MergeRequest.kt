package com.natiqhaciyef.data.network.request

import com.natiqhaciyef.data.network.response.MaterialResponse

data class MergeRequest(
    val title: String,
    val list: List<MaterialResponse>
)