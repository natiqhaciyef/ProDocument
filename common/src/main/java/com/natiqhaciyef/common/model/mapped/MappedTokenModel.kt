package com.natiqhaciyef.common.model.mapped

import com.natiqhaciyef.data.network.response.CRUDResponse

data class MappedTokenModel(
    var uid: String?,
    var result: CRUDResponse?,
    var materialToken: String?,
    var premiumToken: String?,
    var premiumTokenExpiryDate: String?,
    var securityDeviceCode: String?,
)
