package com.natiqhaciyef.domain.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.network.BaseNetworkModel
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