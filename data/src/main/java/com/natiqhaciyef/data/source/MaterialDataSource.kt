package com.natiqhaciyef.data.source

import com.natiqhaciyef.data.network.response.MaterialResponse
import com.natiqhaciyef.data.network.service.MaterialService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MaterialDataSource(
    private val service: MaterialService
) {

    suspend fun getAllFiles(token: String) = withContext(Dispatchers.IO) { service.getMaterials(token = token) }

    suspend fun getFileById(materialId: String, token: String) = withContext(Dispatchers.IO) {
            service.getMaterialById(
                materialId = materialId,
                token = token
            )
        }

    suspend fun createMaterialById(materialModel: MaterialResponse, materialToken: String) = withContext(Dispatchers.IO) {
            service.createMaterialById(
                token = materialToken,
                publishDate = materialModel.publishDate,
                image = materialModel.image,
                title = materialModel.title ?: "",
                description = materialModel.description ?: "",
                type = materialModel.type,
                url = materialModel.url
            )
        }

    suspend fun removeMaterialById(materialToken: String, materialId: String) = withContext(Dispatchers.IO) {
            service.removeMaterialById(
                token = materialToken,
                id = materialId
            )
        }

    suspend fun updateMaterialById(
        materialToken: String,
        materialModel: MaterialResponse
    ) = withContext(Dispatchers.IO) {
            service.updateMaterialById(
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

}