package com.natiqhaciyef.prodocument.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class CRUDResponse(
    @Expose
    val resultCode: Int,
    @Expose
    val message: String,
): Parcelable