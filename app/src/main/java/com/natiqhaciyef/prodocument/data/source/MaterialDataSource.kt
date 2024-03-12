package com.natiqhaciyef.prodocument.data.source

import com.natiqhaciyef.prodocument.data.network.service.MaterialService

class MaterialDataSource(
    private val service: MaterialService
) {

    suspend fun getAllFiles(token: String) =
        service.getMaterials(token = token)

    suspend fun getFileById(materialId: String, token: String) =
        service.getMaterialById(materialId = materialId, token = token)

}