package com.natiqhaciyef.domain.repository.impl

import com.natiqhaciyef.domain.mapper.toMaterialResponse
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.data.source.MaterialDataSource
import com.natiqhaciyef.domain.base.usecase.UseCase
import com.natiqhaciyef.domain.repository.MaterialRepository

class MaterialRepositoryImpl(
    private val ds: MaterialDataSource
) : MaterialRepository {
    override suspend fun getAllMaterials(token: String) =
        ds.getAllFiles(token = token)

    override suspend fun getMaterialById(materialId: String, token: String) =
        ds.getFileById(materialId = materialId, token = token)

    override suspend fun createMaterial(
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

    override suspend fun mergeMaterials(list: List<MappedMaterialModel>) =
        ds.mergeMaterials(list.map { it.toMaterialResponse() })
}