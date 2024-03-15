package com.natiqhaciyef.prodocument.common.mapper

import com.natiqhaciyef.prodocument.data.model.MaterialModel
import com.natiqhaciyef.prodocument.data.network.response.MaterialResponse
import com.natiqhaciyef.prodocument.data.network.response.MaterialsResponse
import com.natiqhaciyef.prodocument.domain.model.UIResult
import com.natiqhaciyef.prodocument.domain.model.mapped.MappedMaterialModel


fun MaterialModel.toMappedMaterial(): MappedMaterialModel? {
    return if (!title.isNullOrEmpty()) {
        MappedMaterialModel(
            id = this.id,
            image = this.image,
            title = this.title!!,
            description = this.description,
            createdDate = this.createdDate,
            type = this.type,
            url = this.url,
        )
    } else null
}

fun MappedMaterialModel.toMaterial(): MaterialModel {
    return MaterialModel(
        id = this.id,
        image = this.image,
        title = this.title,
        description = this.description,
        createdDate = this.createdDate,
        type = this.type,
        url = this.url,
    )
}

fun MaterialResponse.toUIResult(): UIResult<MappedMaterialModel>? {
    return if (!this.title.isNullOrEmpty()) {
        val mappedModel = MappedMaterialModel(
            id = this.id,
            image = this.image,
            title = this.title!!,
            description = this.description,
            createdDate = this.publishDate,
            type = this.type,
            url = this.url,
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

fun MaterialResponse.toMappedModel(): MappedMaterialModel? {
    return if (!this.title.isNullOrEmpty()) {
        MappedMaterialModel(
            id = this.id,
            image = this.image,
            title = this.title!!,
            description = this.description,
            createdDate = this.publishDate,
            type = this.type,
            url = this.url,
        )
    } else {
        null
    }
}

fun MaterialsResponse.toUIResult(): UIResult<List<MappedMaterialModel>>? {
    val materials = this.materials.map { it.toMappedModel() }
    return if (this.materials.isNotEmpty() && materials.contains(null)) {
        UIResult(
            id = this.id,
            publishDate = this.publishDate,
            data = materials.map { it!! }
        )
    } else {
        null
    }
}
