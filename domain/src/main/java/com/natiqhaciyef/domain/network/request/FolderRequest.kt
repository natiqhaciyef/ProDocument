package com.natiqhaciyef.domain.network.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FolderRequest(
    @SerializedName("title")
    var title: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("protectionKey")
    var protectionKey: String? = null,
    @SerializedName("icon")
    var icon: String?,
    @SerializedName("type")
    var type: String,
    @SerializedName("createdDate")
    var createdDate: String,
): Parcelable