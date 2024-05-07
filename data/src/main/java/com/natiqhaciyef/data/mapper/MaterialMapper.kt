package com.natiqhaciyef.data.mapper

import androidx.core.net.toUri
import com.natiqhaciyef.common.model.UIResult
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.data.network.response.MaterialResponse
import com.natiqhaciyef.data.network.response.ListMaterialResponse


fun MaterialResponse.toUIResult(): UIResult<MappedMaterialModel>? {
    return if (!this.title.isNullOrEmpty()) {
        val mappedModel = MappedMaterialModel(
            id = this.id,
            image = this.image,
            title = this.title!!,
            description = this.description,
            createdDate = this.publishDate,
            type = this.type,
            url = this.url.toUri(),
        )

        UIResult(
            id = this.id,
            publishDate = this.publishDate,
            data = mappedModel
        )
    } else {
        null
    }
}

fun MappedMaterialModel.toMaterialResponse(): MaterialResponse {
    return MaterialResponse(
        id = this.id,
        image = this.image,
        title = this.title,
        description = this.description,
        publishDate = createdDate,
        type = this.type,
        url = this.url.toString(),
        result = this.result?.toResponse()
    )
}

fun MaterialResponse.toMappedModel(): MappedMaterialModel? {
    return if (!this.title.isNullOrEmpty()) {
        MappedMaterialModel(
            id = this.id,
            image = this.image,
            title = this.title!!,
            description = this.description,
            createdDate = this.publishDate,
            type = this.type,
            url = this.url.toUri(),
            result = this.result?.toModel()
        )
    } else {
        null
    }
}

fun ListMaterialResponse.toUIResult(): UIResult<List<MappedMaterialModel>>? {
    val materials = this.materials.map { it.toMappedModel() }
    return if (this.materials.isNotEmpty() && !materials.contains(null)) {
        UIResult(
            id = this.id,
            publishDate = this.publishDate,
            data = materials.map { it!! },
            result = this.result?.toModel()
        )
    } else {
        null
    }
}
