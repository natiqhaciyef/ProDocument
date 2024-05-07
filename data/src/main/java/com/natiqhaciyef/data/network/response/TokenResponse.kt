package com.natiqhaciyef.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.natiqhaciyef.data.base.BaseNetworkModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class TokenResponse(
    @SerializedName("accessToken")
    var accessToken: String?,
    @SerializedName("refreshToken")
    var refreshToken: String?,
    @SerializedName("result")
    override var result: CRUDResponse?,
) : BaseNetworkModel, Parcelable