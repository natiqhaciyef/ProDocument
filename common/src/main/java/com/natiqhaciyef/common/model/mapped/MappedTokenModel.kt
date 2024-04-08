package com.natiqhaciyef.common.model.mapped

import com.natiqhaciyef.common.model.CRUDModel


data class MappedTokenModel(
    var uid: String?,
    var result: CRUDModel?,
    var premiumToken: String?,
    var premiumTokenExpiryDate: String?,
    var securityDeviceCode: String?,
)
