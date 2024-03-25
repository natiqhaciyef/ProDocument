package com.natiqhaciyef.common.model

data class UIResult<T>(
    var id: String,
    var data: T,
    var publishDate: String,
    var result: CRUDModel? = null
)
