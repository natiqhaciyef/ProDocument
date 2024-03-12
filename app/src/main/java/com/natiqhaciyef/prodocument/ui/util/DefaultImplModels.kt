package com.natiqhaciyef.prodocument.ui.util

import com.natiqhaciyef.prodocument.domain.model.mapped.MappedUserModel

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
}