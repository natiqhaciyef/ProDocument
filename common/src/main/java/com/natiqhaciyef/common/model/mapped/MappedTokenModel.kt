package com.natiqhaciyef.common.model.mapped

import com.natiqhaciyef.common.model.CRUDModel


data class MappedTokenModel(
    var accessToken: String?,
    var refreshToken: String?,
    var result: CRUDModel?
)
