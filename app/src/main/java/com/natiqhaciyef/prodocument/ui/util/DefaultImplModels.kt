package com.natiqhaciyef.prodocument.ui.util

import androidx.core.net.toUri
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.constants.THOUSAND
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.common.model.StorageSize
import com.natiqhaciyef.common.model.Time
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.model.mapped.MappedSubscriptionModel
import com.natiqhaciyef.common.model.mapped.MappedUserModel
import com.natiqhaciyef.common.model.ui.Color
import com.natiqhaciyef.common.model.ui.SubscriptionType

object DefaultImplModels {
    val mappedSubscriptionModel = MappedSubscriptionModel(
        title = SubscriptionType.OTHER,
        price = ZERO.toDouble(),
        perTime = ONE,
        timeType = Time.MONTH,
        description = EMPTY_STRING,
        features = listOf(),
        backgroundColor = Color.BROWN,
        size = THOUSAND,
        sizeType = StorageSize.MB,
        expireDate = getNow(),
        token = EMPTY_STRING
    )

    val mappedUserModel = MappedUserModel(
        name = EMPTY_STRING,
        password = EMPTY_STRING,
        email = EMPTY_STRING,
        phoneNumber = EMPTY_STRING,
        gender = EMPTY_STRING,
        birthDate = EMPTY_STRING,
        imageUrl = EMPTY_STRING,
        country = EMPTY_STRING,
        city = EMPTY_STRING,
        street = EMPTY_STRING,
        isBiometricEnabled = false,
        subscription = mappedSubscriptionModel,
        reports = listOf(),
    )

    val mappedMaterialModel = MappedMaterialModel(
        id = EMPTY_STRING,
        image = EMPTY_STRING,
        title = EMPTY_STRING,
        description = EMPTY_STRING,
        createdDate = getNow(),
        type = EMPTY_STRING,
        url = EMPTY_STRING.toUri(),
        quality = null,
        isProtected = false,
        protectionKey = null,
    )
}