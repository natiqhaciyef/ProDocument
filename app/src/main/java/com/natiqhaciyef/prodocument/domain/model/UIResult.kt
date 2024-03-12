package com.natiqhaciyef.prodocument.domain.model

data class UIResult<T>(
    var id: String,
    var data: T,
    var publishDate: String
)
