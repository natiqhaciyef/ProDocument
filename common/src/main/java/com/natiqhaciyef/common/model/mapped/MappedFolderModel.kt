package com.natiqhaciyef.common.model.mapped

import android.os.Parcelable
import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.FolderType
import kotlinx.parcelize.Parcelize

@Parcelize
data class MappedFolderModel(
    var id: String,
    var title: String,
    var description: String,
    var protectionKey: String? = null,
    var icon: String? = null,
    var type: FolderType,
    var createdDate: String,
    var filesCount: Int,
    var result: CRUDModel? = null
): Parcelable
