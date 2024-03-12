package com.natiqhaciyef.prodocument.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MaterialModel(
    var id: String = "0",
    var image: String = "",
    var title: String?,
    var description: String?,
    var createdDate: String,
    var type: String,
    var url: String,
) : Parcelable