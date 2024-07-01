package com.natiqhaciyef.data.impl

import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.domain.mapper.toMaterialResponse
import com.natiqhaciyef.domain.network.request.CompressRequest
import com.natiqhaciyef.domain.network.request.ESignRequest
import com.natiqhaciyef.domain.network.request.MergeRequest
import com.natiqhaciyef.domain.network.request.ProtectRequest
import com.natiqhaciyef.domain.network.request.SplitRequest
import com.natiqhaciyef.domain.network.request.WatermarkRequest
import com.natiqhaciyef.data.source.MaterialDataSource
import com.natiqhaciyef.domain.repository.MaterialRepository
import javax.inject.Inject

class MaterialRepositoryImpl @Inject constructor(
    val ds: MaterialDataSource
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

    override suspend fun watermarkMaterial(data: WatermarkRequest) =
        ds.watermarkMaterial(data)

    override suspend fun splitMaterial(data: SplitRequest) =
        ds.splitMaterial(data)

    override suspend fun protectMaterial(data: ProtectRequest) =
        ds.protectMaterial(data)

    override suspend fun compressMaterial(data: CompressRequest) =
        ds.compressMaterial(data)

    override suspend fun eSignMaterial(data: ESignRequest) =
        ds.eSignMaterial(data)
}