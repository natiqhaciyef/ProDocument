package com.natiqhaciyef.domain.repository

import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.data.network.response.CRUDResponse
import com.natiqhaciyef.data.network.response.MaterialResponse
import com.natiqhaciyef.data.network.response.ListMaterialResponse
import com.natiqhaciyef.domain.base.repository.BaseRepository

interface MaterialRepository : BaseRepository {

    suspend fun getAllMaterials(token: String): NetworkResult<ListMaterialResponse>

    suspend fun getMaterialById(materialId: String, token: String): NetworkResult<MaterialResponse>

    suspend fun createMaterial(
        materialModel: MappedMaterialModel,
        materialToken: String
    ): NetworkResult<CRUDResponse>

    suspend fun removeMaterialById(
        materialId: String,
        materialToken: String
    ): NetworkResult<CRUDResponse>

    suspend fun updateMaterialById(
        materialModel: MappedMaterialModel,
        materialToken: String
    ): NetworkResult<CRUDResponse>

    suspend fun mergeMaterials(list: List<MappedMaterialModel>): NetworkResult<MaterialResponse>
}