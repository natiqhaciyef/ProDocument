package com.natiqhaciyef.prodocument.data.network.config

import com.google.gson.annotations.SerializedName
import com.natiqhaciyef.prodocument.data.base.BaseNetworkModel
import com.natiqhaciyef.prodocument.data.base.IOModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(
    @SerializedName("id")
    override var id: Int,
    @SerializedName("user")
    override var data: String,
    @SerializedName("publish_date")
    override var publishDate: String
) : IOModel(), BaseNetworkModel
