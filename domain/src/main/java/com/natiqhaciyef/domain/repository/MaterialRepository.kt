package com.natiqhaciyef.domain.repository

import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.data.network.request.MergeRequest
import com.natiqhaciyef.data.network.response.CRUDResponse
import com.natiqhaciyef.data.network.response.MaterialResponse
import com.natiqhaciyef.data.network.response.ListMaterialResponse
import com.natiqhaciyef.domain.base.repository.BaseRepository

interface MaterialRepository : BaseRepository {

    suspend fun getAllMaterials(email: String): NetworkResult<ListMaterialResponse>

    suspend fun getMaterialById(materialId: String, email: String): NetworkResult<MaterialResponse>

    suspend fun createMaterial(
        materialModel: MappedMaterialModel,
        email: String
    ): NetworkResult<CRUDResponse>

    suspend fun removeMaterialById(
        materialId: String,
        email: String
    ): NetworkResult<CRUDResponse>

    suspend fun updateMaterialById(
        materialModel: MappedMaterialModel,
        email: String
    ): NetworkResult<CRUDResponse>

    suspend fun mergeMaterials(data: MergeRequest): NetworkResult<MaterialResponse>
}