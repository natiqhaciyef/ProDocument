package com.natiqhaciyef.domain.repository.impl

import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.data.mapper.toMaterialResponse
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.data.network.request.MergeRequest
import com.natiqhaciyef.data.network.request.SplitRequest
import com.natiqhaciyef.data.network.request.WatermarkRequest
import com.natiqhaciyef.data.network.response.MaterialResponse
import com.natiqhaciyef.data.source.MaterialDataSource
import com.natiqhaciyef.domain.repository.MaterialRepository

class MaterialRepositoryImpl(
    private val ds: MaterialDataSource
) : MaterialRepository {
    override suspend fun getAllMaterials() =
        ds.getAllFiles()

    override suspend fun getMaterialById(materialId: String) =
        ds.getFileById(materialId = materialId)

    override suspend fun createMaterial(materialModel: MappedMaterialModel) =
        ds.createMaterialById(materialModel = materialModel.toMaterialResponse())

    override suspend fun removeMaterialById(materialId: String) =
        ds.removeMaterialById(materialId = materialId)

    override suspend fun updateMaterialById(materialModel: MappedMaterialModel) =
        ds.updateMaterialById(materialModel = materialModel.toMaterialResponse())

    override suspend fun mergeMaterials(data: MergeRequest) =
        ds.mergeMaterials(data)

    override suspend fun watermarkMaterial(data: WatermarkRequest): NetworkResult<MaterialResponse> =
        ds.watermarkMaterial(data)

    override suspend fun splitMaterial(data: SplitRequest): NetworkResult<List<MaterialResponse>> =
        ds.splitMaterial(data)
}