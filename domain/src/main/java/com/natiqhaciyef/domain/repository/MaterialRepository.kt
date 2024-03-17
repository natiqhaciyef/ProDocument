package com.natiqhaciyef.domain.repository

import com.natiqhaciyef.data.network.response.MaterialResponse
import com.natiqhaciyef.data.network.response.MaterialsResponse
import com.natiqhaciyef.domain.base.BaseRepository

interface MaterialRepository : BaseRepository {

    suspend fun getAllMaterials(token: String): MaterialsResponse?

    suspend fun getMaterialById(materialId: String, token: String): MaterialResponse?
}