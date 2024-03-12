package com.natiqhaciyef.prodocument.domain.repository.impl

import com.natiqhaciyef.prodocument.data.network.response.MaterialResponse
import com.natiqhaciyef.prodocument.data.network.response.MaterialsResponse
import com.natiqhaciyef.prodocument.data.source.MaterialDataSource
import com.natiqhaciyef.prodocument.domain.repository.MaterialRepository

class MaterialRepositoryImpl(
    private val ds: MaterialDataSource
) : MaterialRepository {
    override suspend fun getAllMaterials(token: String): MaterialsResponse? =
        ds.getAllFiles(token = token)

    override suspend fun getMaterialById(materialId: String, token: String): MaterialResponse? =
        ds.getFileById(materialId = materialId, token = token)


}