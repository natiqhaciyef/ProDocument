package com.natiqhaciyef.data.source

import com.natiqhaciyef.common.objects.*
import com.natiqhaciyef.data.base.mock.generateMockerClass
import com.natiqhaciyef.data.mock.materials.CreateMaterialMockGenerator
import com.natiqhaciyef.data.network.LoadType
import com.natiqhaciyef.data.network.handleNetworkResponse
import com.natiqhaciyef.data.network.response.MaterialResponse
import com.natiqhaciyef.data.network.service.MaterialService
import com.natiqhaciyef.data.mock.materials.GetAllMaterialsMockGenerator
import com.natiqhaciyef.data.mock.materials.GetMaterialByIdMockGenerator
import com.natiqhaciyef.data.mock.materials.MergeMaterialsMockGenerator
import com.natiqhaciyef.data.mock.materials.RemoveMaterialMockGenerator
import com.natiqhaciyef.data.mock.materials.UpdateMaterialMockGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MaterialDataSource(
    private val service: MaterialService
) {

    suspend fun getAllFiles(token: String) = withContext(Dispatchers.IO) {
        val mock = generateMockerClass(GetAllMaterialsMockGenerator::class, token)
            .getMock(MATERIAL_TOKEN_MOCK_KEY) { null }

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.getMaterials(token = token)
        }
    }

    suspend fun getFileById(materialId: String, token: String) = withContext(Dispatchers.IO) {
        val map = mapOf(MATERIAL_TOKEN_MOCK_KEY to token, MATERIAL_ID_MOCK_KEY to materialId)
        val mock = generateMockerClass(GetMaterialByIdMockGenerator::class, map)
            .getMock(GetMaterialByIdMockGenerator.customRequest) { null }

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.getMaterialById(
                materialId = materialId,
                token = token
            )
        }
    }

    suspend fun createMaterialById(materialModel: MaterialResponse, materialToken: String) =
        withContext(Dispatchers.IO) {
            val map =
                mapOf(MATERIAL_MOCK_KEY to materialModel, MATERIAL_TOKEN_MOCK_KEY to materialToken)
            val mock = generateMockerClass(CreateMaterialMockGenerator::class, map)
                .getMock(CreateMaterialMockGenerator.customRequest) { null }

            handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
                service.createMaterialById(
                    token = materialToken,
                    publishDate = materialModel.publishDate,
                    image = materialModel.image,
                    title = materialModel.title ?: "",
                    description = materialModel.description ?: "",
                    type = materialModel.type,
                    url = materialModel.url
                )
            }
        }

    suspend fun removeMaterialById(materialToken: String, materialId: String) =
        withContext(Dispatchers.IO) {
            val map =
                mapOf(MATERIAL_TOKEN_MOCK_KEY to materialToken, MATERIAL_ID_MOCK_KEY to materialId)
            val mock = generateMockerClass(RemoveMaterialMockGenerator::class, map)
                .getMock(RemoveMaterialMockGenerator.customRequest) { null }

            handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
                service.removeMaterialById(
                    token = materialToken,
                    id = materialId
                )
            }
        }

    suspend fun updateMaterialById(
        materialToken: String,
        materialModel: MaterialResponse
    ) = withContext(Dispatchers.IO) {
        val map =
            mapOf(MATERIAL_MOCK_KEY to materialModel, MATERIAL_TOKEN_MOCK_KEY to materialToken)
        val mock = generateMockerClass(UpdateMaterialMockGenerator::class, map)
            .getMock(UpdateMaterialMockGenerator.customRequest) { null }

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.updateMaterialById(
                token = materialToken,
                id = materialModel.id,
                publishDate = materialModel.publishDate,
                image = materialModel.image,
                title = materialModel.title ?: "",
                description = materialModel.description ?: "",
                type = materialModel.type,
                url = materialModel.url
            )
        }
    }

    suspend fun mergeMaterials(
        list: List<MaterialResponse>
    ) = withContext(Dispatchers.IO) {
        val mock = generateMockerClass(MergeMaterialsMockGenerator::class, list)
            .getMock(MergeMaterialsMockGenerator.customRequest) { null }

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.mergeMaterials(materials = list)
        }
    }

}