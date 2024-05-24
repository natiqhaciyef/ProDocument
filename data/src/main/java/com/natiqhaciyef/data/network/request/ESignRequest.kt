package com.natiqhaciyef.data.network.request

import android.graphics.Bitmap
import com.natiqhaciyef.data.network.response.MaterialResponse

data class ESignRequest(
    val sign: String,
    val signBitmap: String,
    val material: MaterialResponse
)
