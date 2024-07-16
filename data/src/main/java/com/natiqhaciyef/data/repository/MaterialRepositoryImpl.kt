package com.natiqhaciyef.data.repository

import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.network.NetworkResult
import com.natiqhaciyef.domain.mapper.toMaterialResponse
import com.natiqhaciyef.domain.network.request.CompressRequest
import com.natiqhaciyef.domain.network.request.ESignRequest
import com.natiqhaciyef.domain.network.request.MergeRequest
import com.natiqhaciyef.domain.network.request.ProtectRequest
import com.natiqhaciyef.domain.network.request.SplitRequest
import com.natiqhaciyef.domain.network.request.WatermarkRequest
import com.natiqhaciyef.data.source.MaterialDataSource
import com.natiqhaciyef.domain.network.request.FolderRequest
import com.natiqhaciyef.domain.network.response.FolderResponse
import com.natiqhaciyef.domain.network.response.MaterialResponse
import com.natiqhaciyef.domain.repository.MaterialRepository
import javax.inject.Inject

class MaterialRepositoryImpl @Inject constructor(
    val ds: MaterialDataSource
) : MaterialRepository {
    override suspend fun getAllMaterials() =
        ds.getAllFiles()

    override suspend fun getAllMaterialsWithoutFolder(): NetworkResult<List<MaterialResponse>> =
        ds.getAllFilesWithoutFolder()

    override suspend fun getMaterialById(materialId: String) =
        ds.getFileById(materialId = materialId)

    override suspend fun getAllFolders() =
        ds.getAllFolders()

    override suspend fun getFolderById(folderId: String): NetworkResult<FolderResponse> =
        ds.getFolderById(folderId = folderId)

    override suspend fun getMaterialsByFolderId(folderId: String): NetworkResult<List<MaterialResponse>> =
        ds.getMaterialsByFolderId(folderId = folderId)

    override suspend fun createMaterial(materialModel: MappedMaterialModel) =
        ds.createMaterialById(materialModel = materialModel.toMaterialResponse())

    override suspend fun createFolder(folderRequest: FolderRequest): NetworkResult<CRUDResponse> =
        ds.createFolder(folderRequest = folderRequest)

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