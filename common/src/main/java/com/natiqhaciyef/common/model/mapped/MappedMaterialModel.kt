package com.natiqhaciyef.common.model.mapped

import android.net.Uri
import android.os.Parcelable
import com.natiqhaciyef.common.model.CRUDModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class MappedMaterialModel(
    var id: String,
    var image: String,
    var title: String,
    var description: String?,
    var createdDate: String,
    var type: String,
    var url: Uri,
    var downloadedUri: String? = null,
    var isDownloading: Boolean = false,
    var result: CRUDModel? = null
): Parcelable
