package com.natiqhaciyef.common.model.mapped

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MappedMaterialModel(
    var id: String,
    var image: String,
    var title: String,
    var description: String?,
    var createdDate: String,
    var type: String,
    var url: String,
    var downloadedUri: String? = null,
    var isDownloading: Boolean = false,
): Parcelable
