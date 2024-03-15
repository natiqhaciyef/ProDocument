package com.natiqhaciyef.domain.repository.impl

import com.natiqhaciyef.data.network.response.MaterialResponse
import com.natiqhaciyef.data.network.response.MaterialsResponse
import com.natiqhaciyef.data.source.MaterialDataSource
import com.natiqhaciyef.domain.repository.MaterialRepository

class MaterialRepositoryImpl(
    private val ds: MaterialDataSource
) : MaterialRepository {
    override suspend fun getAllMaterials(token: String): MaterialsResponse? =
        ds.getAllFiles(token = token)

    override suspend fun getMaterialById(materialId: String, token: String): MaterialResponse? =
        ds.getFileById(materialId = materialId, token = token)


}