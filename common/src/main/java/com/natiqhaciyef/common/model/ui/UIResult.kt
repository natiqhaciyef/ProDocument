package com.natiqhaciyef.common.model.ui

import com.natiqhaciyef.common.model.CRUDModel

data class UIResult<T>(
    var id: String,
    var data: T,
    var publishDate: String,
    var result: CRUDModel? = null
)
