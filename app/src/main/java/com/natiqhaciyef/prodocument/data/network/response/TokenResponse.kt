package com.natiqhaciyef.prodocument.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.natiqhaciyef.prodocument.data.base.BaseNetworkModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class TokenResponse(
    @SerializedName("token")
    var uid: String?,
    @SerializedName("result")
    override var result: CRUDResponse?
) : BaseNetworkModel, Parcelable