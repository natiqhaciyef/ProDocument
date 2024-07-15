package com.natiqhaciyef.data.source

import com.natiqhaciyef.common.constants.*
import com.natiqhaciyef.core.base.mock.generateMockerClass
import com.natiqhaciyef.data.mock.materials.CompressMaterialMockGenerator
import com.natiqhaciyef.data.mock.materials.CreateMaterialMockGenerator
import com.natiqhaciyef.data.mock.materials.ESignMaterialMockGenerator
import com.natiqhaciyef.core.base.network.LoadType
import com.natiqhaciyef.core.base.network.handleNetworkResponse
import com.natiqhaciyef.data.mock.materials.GetAllFoldersMockGenerator
import com.natiqhaciyef.domain.network.response.MaterialResponse
import com.natiqhaciyef.data.network.service.MaterialService
import com.natiqhaciyef.data.mock.materials.GetAllMaterialsMockGenerator
import com.natiqhaciyef.data.mock.materials.GetAllMaterialsWithoutFolderMockGenerator
import com.natiqhaciyef.data.mock.materials.GetFolderByIdMockGenerator
import com.natiqhaciyef.data.mock.materials.GetMaterialByIdMockGenerator
import com.natiqhaciyef.data.mock.materials.GetMaterialsByFolderIdMockGenerator
import com.natiqhaciyef.data.mock.materials.MergeMaterialsMockGenerator
import com.natiqhaciyef.data.mock.materials.ProtectMaterialMockGenerator
import com.natiqhaciyef.data.mock.materials.RemoveMaterialMockGenerator
import com.natiqhaciyef.data.mock.materials.SplitMaterialMockGenerator
import com.natiqhaciyef.data.mock.materials.UpdateMaterialMockGenerator
import com.natiqhaciyef.data.mock.materials.WatermarkMaterialMockGenerator
import com.natiqhaciyef.data.network.manager.TokenManager
import com.natiqhaciyef.domain.network.request.CompressRequest
import com.natiqhaciyef.domain.network.request.ESignRequest
import com.natiqhaciyef.domain.network.request.MergeRequest
import com.natiqhaciyef.domain.network.request.ProtectRequest
import com.natiqhaciyef.domain.network.request.SplitRequest
import com.natiqhaciyef.domain.network.request.WatermarkRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MaterialDataSource @Inject constructor(
    private val manager: TokenManager,
    private val service: MaterialService
) {

    suspend fun getAllFiles() = withContext(Dispatchers.IO) {
        val requestHeader = manager.generateToken()
        val mock = generateMockerClass(GetAllMaterialsMockGenerator::class, Unit)
            .getMock(null)

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.getMaterials(token = requestHeader)
        }
    }

    suspend fun getAllFilesWithoutFolder() = withContext(Dispatchers.IO) {
        val requestHeader = manager.generateToken()
        val mock = generateMockerClass(GetAllMaterialsWithoutFolderMockGenerator::class, Unit)
            .getMock(null)

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.getMaterialsWithoutFolder(token = requestHeader)
        }
    }


    suspend fun getFileById(materialId: String) = withContext(Dispatchers.IO) {
        val requestHeader = manager.generateToken()
        val mock = generateMockerClass(GetMaterialByIdMockGenerator::class, materialId)
            .getMock(null)

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.getMaterialById(materialId = materialId, token = requestHeader)
        }
    }

    suspend fun getAllFolders() = withContext(Dispatchers.IO){
        val requestHeader = manager.generateToken()
        val mock = generateMockerClass(GetAllFoldersMockGenerator::class, Unit)
            .getMock { null }
        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.getAllFolders(token = requestHeader)
        }
    }

    suspend fun getFolderById(folderId: String) = withContext(Dispatchers.IO){
        val requestHeader = manager.generateToken()
        val mock = generateMockerClass(GetFolderByIdMockGenerator::class, folderId)
            .getMock { null }
        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.getFolderById(token = requestHeader, folderId = folderId)
        }
    }

    suspend fun getMaterialsByFolderId(folderId: String) = withContext(Dispatchers.IO){
        val requestHeader = manager.generateToken()
        val mock = generateMockerClass(GetMaterialsByFolderIdMockGenerator::class, folderId)
            .getMock { null }
        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.getMaterialsByFolderId(token = requestHeader, folderId = folderId)
        }
    }

    suspend fun createMaterialById(materialModel: MaterialResponse) =
        withContext(Dispatchers.IO) {
            val requestHeader = manager.generateToken()

            val mock = generateMockerClass(CreateMaterialMockGenerator::class, materialModel)
                .getMock(null)

            handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
                service.createMaterialById(
                    token = requestHeader,
                    publishDate = materialModel.publishDate,
                    image = materialModel.image,
                    title = materialModel.title ?: EMPTY_STRING,
                    description = materialModel.description ?: EMPTY_STRING,
                    type = materialModel.type,
                    url = materialModel.url
                )
            }
        }

    suspend fun removeMaterialById(materialId: String) =
        withContext(Dispatchers.IO) {
            val requestHeader = manager.generateToken()
            val mock = generateMockerClass(RemoveMaterialMockGenerator::class, materialId)
                .getMock(null)

            handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
                service.removeMaterialById(
                    token = requestHeader,
                    id = materialId
                )
            }
        }

    suspend fun updateMaterialById(materialModel: MaterialResponse) =
        withContext(Dispatchers.IO) {
            val requestHeader = manager.generateToken()
            val mock = generateMockerClass(UpdateMaterialMockGenerator::class,materialModel)
                .getMock(null)

            handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
                service.updateMaterialById(
                    token = requestHeader,
                    id = materialModel.id,
                    publishDate = materialModel.publishDate,
                    image = materialModel.image,
                    title = materialModel.title ?: EMPTY_STRING,
                    description = materialModel.description ?: EMPTY_STRING,
                    type = materialModel.type,
                    url = materialModel.url
                )
            }
        }

    suspend fun mergeMaterials(
        data: MergeRequest
    ) = withContext(Dispatchers.IO) {
        val requestHeader = manager.generateToken()
        val mock = generateMockerClass(MergeMaterialsMockGenerator::class, data)
            .getMock(null)

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.mergeMaterials(token = requestHeader, data = data)
        }
    }

    suspend fun watermarkMaterial(data: WatermarkRequest) =
        withContext(Dispatchers.IO) {
            val requestHeader = manager.generateToken()
            val mock = generateMockerClass(WatermarkMaterialMockGenerator::class, data)
                .getMock(null)

            handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
                service.watermarkMaterial(token = requestHeader, data = data)
            }
        }

    suspend fun splitMaterial(
        data: SplitRequest
    ) = withContext(Dispatchers.IO) {
        val requestHeader = manager.generateToken()
        val mock = generateMockerClass(SplitMaterialMockGenerator::class, data)
            .getMock(null)

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.splitMaterial(token = requestHeader, data = data)
        }
    }

    suspend fun protectMaterial(
        data: ProtectRequest
    ) = withContext(Dispatchers.IO) {
        val requestHeader = manager.generateToken()
        val mock = generateMockerClass(ProtectMaterialMockGenerator::class, data)
            .getMock(null)

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.protectMaterial(token = requestHeader, data = data)
        }
    }

    suspend fun compressMaterial(
        data: CompressRequest
    ) = withContext(Dispatchers.IO){
        val requestHeader = manager.generateToken()
        val mock = generateMockerClass(CompressMaterialMockGenerator::class, data)
            .getMock(null)

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.compressMaterial(token = requestHeader, data = data)
        }
    }

    suspend fun eSignMaterial(
        data: ESignRequest
    ) = withContext(Dispatchers.IO){
        val requestHeader = manager.generateToken()
        val mock = generateMockerClass(ESignMaterialMockGenerator::class, data)
            .getMock(null)

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.eSignMaterial(token = requestHeader, data = data)
        }
    }
}