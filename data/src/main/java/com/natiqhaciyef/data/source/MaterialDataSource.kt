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
import com.natiqhaciyef.data.network.request.MergeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MaterialDataSource(
    private val service: MaterialService
) {

    suspend fun getAllFiles(email: String) = withContext(Dispatchers.IO) {
        val mock = generateMockerClass(GetAllMaterialsMockGenerator::class, email)
            .getMock(USER_EMAIL_MOCK_KEY) { null }

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.getMaterials(email = email)
        }
    }

    suspend fun getFileById(materialId: String, email: String) = withContext(Dispatchers.IO) {
        val map = mapOf(USER_EMAIL_MOCK_KEY to email, MATERIAL_ID_MOCK_KEY to materialId)
        val mock = generateMockerClass(GetMaterialByIdMockGenerator::class, map)
            .getMock(GetMaterialByIdMockGenerator.customRequest) { null }

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.getMaterialById(
                materialId = materialId,
                email = email
            )
        }
    }

    suspend fun createMaterialById(materialModel: MaterialResponse, email: String) =
        withContext(Dispatchers.IO) {
            val map =
                mapOf(MATERIAL_MOCK_KEY to materialModel, USER_EMAIL_MOCK_KEY to email)

            val mock = generateMockerClass(CreateMaterialMockGenerator::class, map)
                .getMock(CreateMaterialMockGenerator.customRequest) { null }

            handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
                service.createMaterialById(
                    email = email,
                    publishDate = materialModel.publishDate,
                    image = materialModel.image,
                    title = materialModel.title ?: "",
                    description = materialModel.description ?: "",
                    type = materialModel.type,
                    url = materialModel.url
                )
            }
        }

    suspend fun removeMaterialById(email: String, materialId: String) =
        withContext(Dispatchers.IO) {
            val map =
                mapOf(USER_EMAIL_MOCK_KEY to email, MATERIAL_ID_MOCK_KEY to materialId)
            val mock = generateMockerClass(RemoveMaterialMockGenerator::class, map)
                .getMock(RemoveMaterialMockGenerator.customRequest) { null }

            handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
                service.removeMaterialById(
                    email = email,
                    id = materialId
                )
            }
        }

    suspend fun updateMaterialById(
        email: String,
        materialModel: MaterialResponse
    ) = withContext(Dispatchers.IO) {
        val map =
            mapOf(MATERIAL_MOCK_KEY to materialModel, USER_EMAIL_MOCK_KEY to email)
        val mock = generateMockerClass(UpdateMaterialMockGenerator::class, map)
            .getMock(UpdateMaterialMockGenerator.customRequest) { null }

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.updateMaterialById(
                email = email,
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
        data: MergeRequest
    ) = withContext(Dispatchers.IO) {
        val mock = generateMockerClass(MergeMaterialsMockGenerator::class, data)
            .getMock(MergeMaterialsMockGenerator.customRequest) { null }

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.mergeMaterials(data = data)
        }
    }

}