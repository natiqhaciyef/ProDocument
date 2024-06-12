package com.natiqhaciyef.data.mapper

import com.natiqhaciyef.common.model.ui.Color
import com.natiqhaciyef.common.model.ui.SubscriptionType
import com.natiqhaciyef.common.model.Time
import com.natiqhaciyef.common.model.mapped.MappedSubscriptionModel
import com.natiqhaciyef.data.network.response.SubscriptionResponse


fun SubscriptionResponse.toMapped(): MappedSubscriptionModel? {
    return if (
        this.title.isNotEmpty()
        && this.price != 0.0
        && this.perTime != 0
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
            token = this.token
        )
    else
        null
}

fun MappedSubscriptionModel.toResponse(): SubscriptionResponse? {
    return if (
        this.price != 0.0
        && this.perTime != 0
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
            token = this.token
        )
    else
        null
}


