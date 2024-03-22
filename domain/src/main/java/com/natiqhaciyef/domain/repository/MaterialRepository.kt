package com.natiqhaciyef.domain.repository

import com.natiqhaciyef.data.network.response.MaterialResponse
import com.natiqhaciyef.data.network.response.ListMaterialResponse
import com.natiqhaciyef.domain.base.BaseRepository

interface MaterialRepository : BaseRepository {

    suspend fun getAllMaterials(token: String): ListMaterialResponse?

    suspend fun getMaterialById(materialId: String, token: String): MaterialResponse?
}