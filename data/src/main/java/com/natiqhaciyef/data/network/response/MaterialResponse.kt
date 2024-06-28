package com.natiqhaciyef.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.network.BaseNetworkModel
import com.natiqhaciyef.core.base.network.IOModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class MaterialResponse(
    @SerializedName("id")
    override var id: String,
    @SerializedName("publish_date")
    override var publishDate: String,
    @SerializedName("image")
    var image: String = EMPTY_STRING,
    @SerializedName("title")
    var title: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("type")
    var type: String,
    @SerializedName("url")
    var url: String,
    @SerializedName("quality")
    var quality: String? = null,
    @SerializedName("protectionKey")
    var protectionKey: String? = null,
    @SerializedName("result")
    override var result: CRUDResponse? = null,
) : IOModel(), BaseNetworkModel, Parcelable


@Parcelize
data class ListMaterialResponse(
    @SerializedName("materials")
    var materials: List<MaterialResponse>,
    @SerializedName("id")
    override var id: String,
    @SerializedName("result")
    override var result: CRUDResponse?,
    @SerializedName("publish_date")
    override var publishDate: String
) : IOModel(), BaseNetworkModel