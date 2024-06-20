package com.natiqhaciyef.data.source

import com.natiqhaciyef.common.constants.*
import com.natiqhaciyef.core.base.mock.generateMockerClass
import com.natiqhaciyef.data.mock.materials.CompressMaterialMockGenerator
import com.natiqhaciyef.data.mock.materials.CreateMaterialMockGenerator
import com.natiqhaciyef.data.mock.materials.ESignMaterialMockGenerator
import com.natiqhaciyef.data.network.LoadType
import com.natiqhaciyef.data.network.handleNetworkResponse
import com.natiqhaciyef.data.network.response.MaterialResponse
import com.natiqhaciyef.data.network.service.MaterialService
import com.natiqhaciyef.data.mock.materials.GetAllMaterialsMockGenerator
import com.natiqhaciyef.data.mock.materials.GetMaterialByIdMockGenerator
import com.natiqhaciyef.data.mock.materials.MergeMaterialsMockGenerator
import com.natiqhaciyef.data.mock.materials.ProtectMaterialMockGenerator
import com.natiqhaciyef.data.mock.materials.RemoveMaterialMockGenerator
import com.natiqhaciyef.data.mock.materials.SplitMaterialMockGenerator
import com.natiqhaciyef.data.mock.materials.UpdateMaterialMockGenerator
import com.natiqhaciyef.data.mock.materials.WatermarkMaterialMockGenerator
import com.natiqhaciyef.data.network.NetworkConfig
import com.natiqhaciyef.data.network.manager.TokenManager
import com.natiqhaciyef.data.network.request.CompressRequest
import com.natiqhaciyef.data.network.request.ESignRequest
import com.natiqhaciyef.data.network.request.MergeRequest
import com.natiqhaciyef.data.network.request.ProtectRequest
import com.natiqhaciyef.data.network.request.SplitRequest
import com.natiqhaciyef.data.network.request.WatermarkRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MaterialDataSource(
    private val manager: TokenManager,
    private val service: MaterialService
) {

    suspend fun getAllFiles() = withContext(Dispatchers.IO) {
        val mock = generateMockerClass(GetAllMaterialsMockGenerator::class, manager.generateToken())
            .getMock(NetworkConfig.HEADER_AUTHORIZATION_TYPE + MATERIAL_TOKEN_MOCK_KEY) { null }

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.getMaterials(token = manager.generateToken())
        }
    }

    suspend fun getFileById(materialId: String) = withContext(Dispatchers.IO) {
        val requestHeader = manager.generateToken()
        val map =
            mapOf(MATERIAL_TOKEN_MOCK_KEY to requestHeader, MATERIAL_ID_MOCK_KEY to materialId)
        val mock = generateMockerClass(GetMaterialByIdMockGenerator::class, map)
            .getMock(GetMaterialByIdMockGenerator.customRequest) { null }

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.getMaterialById(materialId = materialId, token = requestHeader)
        }
    }

    suspend fun createMaterialById(materialModel: MaterialResponse) =
        withContext(Dispatchers.IO) {
            val requestHeader = manager.generateToken()
            val map =
                mapOf(MATERIAL_MOCK_KEY to materialModel, MATERIAL_TOKEN_MOCK_KEY to requestHeader)

            val mock = generateMockerClass(CreateMaterialMockGenerator::class, map)
                .getMock(CreateMaterialMockGenerator.customRequest) { null }

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
            val map =
                mapOf(MATERIAL_TOKEN_MOCK_KEY to requestHeader, MATERIAL_ID_MOCK_KEY to materialId)
            val mock = generateMockerClass(RemoveMaterialMockGenerator::class, map)
                .getMock(RemoveMaterialMockGenerator.customRequest) { null }

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
            val map =
                mapOf(MATERIAL_MOCK_KEY to materialModel, MATERIAL_TOKEN_MOCK_KEY to requestHeader)
            val mock = generateMockerClass(UpdateMaterialMockGenerator::class, map)
                .getMock(UpdateMaterialMockGenerator.customRequest) { null }

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
            .getMock(MergeMaterialsMockGenerator.customRequest) { null }

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.mergeMaterials(token = requestHeader, data = data)
        }
    }

    suspend fun watermarkMaterial(data: WatermarkRequest) =
        withContext(Dispatchers.IO) {
            val requestHeader = manager.generateToken()
            val mock = generateMockerClass(WatermarkMaterialMockGenerator::class, data)
                .getMock(WatermarkMaterialMockGenerator.customRequest) { null }

            handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
                service.watermarkMaterial(token = requestHeader, data = data)
            }
        }

    suspend fun splitMaterial(
        data: SplitRequest
    ) = withContext(Dispatchers.IO) {
        val requestHeader = manager.generateToken()
        val mock = generateMockerClass(SplitMaterialMockGenerator::class, data)
            .getMock(SplitMaterialMockGenerator.customRequest) { null }

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.splitMaterial(token = requestHeader, data = data)
        }
    }

    suspend fun protectMaterial(
        data: ProtectRequest
    ) = withContext(Dispatchers.IO) {
        val requestHeader = manager.generateToken()
        val mock = generateMockerClass(ProtectMaterialMockGenerator::class, data)
            .getMock(ProtectMaterialMockGenerator.customRequest) { null }

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.protectMaterial(token = requestHeader, data = data)
        }
    }

    suspend fun compressMaterial(
        data: CompressRequest
    ) = withContext(Dispatchers.IO){
        val requestHeader = manager.generateToken()
        val mock = generateMockerClass(CompressMaterialMockGenerator::class, data)
            .getMock(CompressMaterialMockGenerator.customRequest) { null }

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.compressMaterial(token = requestHeader, data = data)
        }
    }

    suspend fun eSignMaterial(
        data: ESignRequest
    ) = withContext(Dispatchers.IO){
        val requestHeader = manager.generateToken()
        val mock = generateMockerClass(ESignMaterialMockGenerator::class, data)
            .getMock(ESignMaterialMockGenerator.customRequest) { null }

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.eSignMaterial(token = requestHeader, data = data)
        }
    }
}