package com.natiqhaciyef.domain.repository

import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.data.network.response.CRUDResponse
import com.natiqhaciyef.data.network.response.MaterialResponse
import com.natiqhaciyef.data.network.response.ListMaterialResponse
import com.natiqhaciyef.domain.base.BaseRepository

interface MaterialRepository : BaseRepository {

    suspend fun getAllMaterials(token: String): ListMaterialResponse?

    suspend fun getMaterialById(materialId: String, token: String): MaterialResponse?

    suspend fun createMaterialById(
        materialModel: MappedMaterialModel,
        materialToken: String
    ): CRUDResponse?

    suspend fun removeMaterialById(
        materialId: String,
        materialToken: String
    ): CRUDResponse?

    suspend fun updateMaterialById(
        materialModel: MappedMaterialModel,
        materialToken: String
    ): CRUDResponse?
}