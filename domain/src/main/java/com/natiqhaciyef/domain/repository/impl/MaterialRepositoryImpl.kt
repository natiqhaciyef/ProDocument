package com.natiqhaciyef.domain.repository.impl

import com.natiqhaciyef.common.mapper.toMaterialResponse
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.data.source.MaterialDataSource
import com.natiqhaciyef.domain.repository.MaterialRepository

class MaterialRepositoryImpl(
    private val ds: MaterialDataSource
) : MaterialRepository {
    override suspend fun getAllMaterials(token: String) =
        ds.getAllFiles(token = token)

    override suspend fun getMaterialById(materialId: String, token: String) =
        ds.getFileById(materialId = materialId, token = token)

    override suspend fun createMaterialById(
        materialModel: MappedMaterialModel,
        materialToken: String
    ) = ds.createMaterialById(
            materialToken = materialToken,
            materialModel = materialModel.toMaterialResponse()
        )

    override suspend fun removeMaterialById(
        materialId: String,
        materialToken: String
    ) = ds.removeMaterialById(materialToken = materialToken, materialId = materialId)

    override suspend fun updateMaterialById(
        materialModel: MappedMaterialModel,
        materialToken: String
    ) = ds.updateMaterialById(
        materialToken = materialToken,
        materialModel = materialModel.toMaterialResponse()
    )

}