package com.natiqhaciyef.data.source

import com.natiqhaciyef.data.network.response.MaterialResponse
import com.natiqhaciyef.data.network.service.MaterialService

class MaterialDataSource(
    private val service: MaterialService
) {

    suspend fun getAllFiles(token: String) =
        service.getMaterials(token = token)

    suspend fun getFileById(materialId: String, token: String) =
        service.getMaterialById(materialId = materialId, token = token)

    suspend fun createMaterialByToken(materialModel: MaterialResponse, materialToken: String) =
        service.createMaterialByToken(
            token = materialToken,
            id = materialModel.id,
            publishDate = materialModel.publishDate,
            image = materialModel.image,
            title = materialModel.title ?: "",
            description = materialModel.description ?: "",
            type = materialModel.type,
            url = materialModel.url
        )

    suspend fun removeMaterialByToken(materialToken: String, materialId: String) =
        service.removeMaterialByToken(token = materialToken, id = materialId)

    suspend fun updateMaterialByToken(
        materialToken: String,
        materialModel: MaterialResponse
    ) =
        service.updateMaterialByToken(
            token = materialToken,
            id = materialModel.id,
            publishDate = materialModel.publishDate,
            image = materialModel.image,
            title = materialModel.title ?: "",
            description = materialModel.description ?: "",
            type = materialModel.type,
            url = materialModel.url
        )
}