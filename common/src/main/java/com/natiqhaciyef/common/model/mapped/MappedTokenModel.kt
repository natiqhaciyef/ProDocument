package com.natiqhaciyef.common.model.mapped

import com.natiqhaciyef.common.model.CRUDModel


data class MappedTokenModel(
    var uid: String,
    var email: String,
    var result: CRUDModel?
)
