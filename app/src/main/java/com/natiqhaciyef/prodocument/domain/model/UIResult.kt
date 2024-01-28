package com.natiqhaciyef.prodocument.domain.model

data class UIResult<T>(
    var id: Int,
    var data: T,
    var publishDate: String
)
