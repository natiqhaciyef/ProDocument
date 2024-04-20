package com.natiqhaciyef.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.natiqhaciyef.data.base.BaseNetworkModel
import com.natiqhaciyef.data.network.response.CRUDResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class TokenResponse(
    @SerializedName("token")
    var uid: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("result")
    override var result: CRUDResponse?,
) : BaseNetworkModel, Parcelable