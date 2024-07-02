package com.natiqhaciyef.domain.mapper

import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.common.model.StorageSize
import com.natiqhaciyef.common.model.ui.Color
import com.natiqhaciyef.common.model.ui.SubscriptionType
import com.natiqhaciyef.common.model.Time
import com.natiqhaciyef.common.model.mapped.MappedSubscriptionModel
import com.natiqhaciyef.domain.network.response.SubscriptionResponse


fun SubscriptionResponse.toMapped(): MappedSubscriptionModel? {
    return if (
        this.title.isNotEmpty()
        && this.price != ZERO.toDouble()
        && this.perTime != ZERO
        && this.timeType.isNotEmpty()
        && this.features.isNotEmpty()
        && this.expireDate.isNotEmpty()
        && this.token.isNotEmpty()
    )
        MappedSubscriptionModel(
            title = SubscriptionType.stringToSubscriptionType(this.title),
            price = this.price,
            perTime = this.perTime,
            timeType = Time.stringToTimeType(this.timeType),
            description = this.description,
            features = this.features,
            backgroundColor = Color.stringToColor(this.backgroundColor),
            expireDate = this.expireDate,
            size = this.size,
            sizeType = StorageSize.stringToStorageSize(this.sizeType),
            token = this.token
        )
    else
        null
}

fun MappedSubscriptionModel.toResponse(): SubscriptionResponse? {
    return if (
        this.price != ZERO.toDouble()
        && this.perTime != ZERO
        && this.features.isNotEmpty()
        && this.expireDate.isNotEmpty()
        && this.token.isNotEmpty()
    )
        SubscriptionResponse(
            title = this.title.name.lowercase(),
            price = this.price,
            perTime = this.perTime,
            timeType = this.timeType.name.lowercase(),
            description = this.description,
            features = this.features,
            backgroundColor = this.backgroundColor.name.lowercase(),
            expireDate = this.expireDate,
            size = this.size,
            sizeType = this.sizeType.name,
            token = this.token
        )
    else
        null
}


