package com.natiqhaciyef.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.natiqhaciyef.data.base.BaseNetworkModel
import com.natiqhaciyef.data.base.IOModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class MaterialResponse(
    @SerializedName("id")
    override var id: String,
    @SerializedName("publish_date")
    override var publishDate: String,
    @SerializedName("image")
    var image: String = "",
    @SerializedName("title")
    var title: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("type")
    var type: String,
    @SerializedName("url")
    var url: String,
    override var result: CRUDResponse? = null,
) : IOModel(), BaseNetworkModel, Parcelable


@Parcelize
data class ListMaterialResponse(
    @SerializedName("materials")
    var materials: List<MaterialResponse>,
    @SerializedName("id")
    override var id: String,
    override var result: CRUDResponse?,
    @SerializedName("publish_date")
    override var publishDate: String
) : IOModel(), BaseNetworkModel