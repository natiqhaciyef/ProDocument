package com.natiqhaciyef.data.network.request

import com.natiqhaciyef.common.model.mapped.MappedMaterialModel

data class CompressRequest(
    var material: MappedMaterialModel,
    var quality: String
)
