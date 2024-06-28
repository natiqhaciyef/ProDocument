package com.natiqhaciyef.prodocument.ui.util

import androidx.core.net.toUri
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.model.mapped.MappedUserModel

object DefaultImplModels {
    val mappedUserModel = MappedUserModel(
        name = EMPTY_STRING,
        password = EMPTY_STRING,
        email = EMPTY_STRING,
        phoneNumber = EMPTY_STRING,
        gender = EMPTY_STRING,
        birthDate = EMPTY_STRING,
        imageUrl = EMPTY_STRING
    )

    val mappedMaterialModel = MappedMaterialModel(
        id = "$ZERO",
        image = EMPTY_STRING,
        title = EMPTY_STRING,
        description = null,
        createdDate = EMPTY_STRING,
        type = EMPTY_STRING,
        url = EMPTY_STRING.toUri(),
        result = null
    )
}