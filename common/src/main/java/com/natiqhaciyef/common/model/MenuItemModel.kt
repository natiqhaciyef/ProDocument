package com.natiqhaciyef.common.model


data class MenuItemModel(
    var title: String,
    var imageId: Int,
    var routeTitle: String,
    var type: String? = null
)

