package com.natiqhaciyef.prodocument.data.network.response

import com.google.gson.annotations.Expose


data class CRUDResponse(
    @Expose
    val success: Int,
    @Expose
    val message: String
)