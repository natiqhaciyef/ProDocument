package com.natiqhaciyef.data.network.request

import com.natiqhaciyef.common.model.mapped.MappedMaterialModel

data class ProtectRequest(
    val material: MappedMaterialModel,
    val key: String
)