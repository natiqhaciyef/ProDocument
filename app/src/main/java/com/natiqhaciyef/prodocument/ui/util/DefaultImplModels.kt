package com.natiqhaciyef.prodocument.ui.util

import androidx.core.net.toUri
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.model.mapped.MappedUserModel

object DefaultImplModels {
    val mappedUserModel = MappedUserModel(
        name = "",
        password = "",
        email = "",
        phoneNumber = "",
        gender = "",
        birthDate = "",
        imageUrl = ""
    )

    val mappedMaterialModel = MappedMaterialModel(
        id = "0",
        image = "",
        title = "",
        description = null,
        createdDate = "",
        type = "",
        url = "".toUri(),
        result = null
    )
}