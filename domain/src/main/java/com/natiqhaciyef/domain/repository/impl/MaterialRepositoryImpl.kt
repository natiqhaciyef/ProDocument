package com.natiqhaciyef.domain.repository.impl

import com.natiqhaciyef.data.model.MaterialModel
import com.natiqhaciyef.data.network.response.CRUDResponse
import com.natiqhaciyef.data.network.response.MaterialResponse
import com.natiqhaciyef.data.network.response.ListMaterialResponse
import com.natiqhaciyef.data.source.MaterialDataSource
import com.natiqhaciyef.domain.repository.MaterialRepository

class MaterialRepositoryImpl(
    private val ds: MaterialDataSource
) : MaterialRepository {
    override suspend fun getAllMaterials(token: String): ListMaterialResponse? =
        ds.getAllFiles(token = token)

    override suspend fun getMaterialById(materialId: String, token: String): MaterialResponse? =
        ds.getFileById(materialId = materialId, token = token)

    override suspend fun createMaterialByToken(
        materialModel: MaterialModel,
        materialToken: String
    ) =
        ds.createMaterialByToken(materialToken = materialToken, materialModel = materialModel)

    override suspend fun removeMaterialByToken(
        materialId: String,
        materialToken: String
    ): CRUDResponse? =
        ds.removeMaterialByToken(materialToken = materialToken, materialId = materialId)

    override suspend fun updateMaterialByToken(
        materialModel: MaterialModel,
        materialToken: String
    ): CRUDResponse? = ds.updateMaterialByToken(
        materialToken = materialToken,
        materialModel = materialModel
    )

}