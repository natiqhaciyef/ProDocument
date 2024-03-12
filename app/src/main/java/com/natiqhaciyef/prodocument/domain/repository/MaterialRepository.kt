package com.natiqhaciyef.prodocument.domain.repository

import com.natiqhaciyef.prodocument.data.network.response.MaterialResponse
import com.natiqhaciyef.prodocument.data.network.response.MaterialsResponse
import com.natiqhaciyef.prodocument.domain.base.BaseRepository

interface MaterialRepository : BaseRepository{

    suspend fun getAllMaterials(token: String): MaterialsResponse?

    suspend fun getMaterialById(materialId: String, token: String): MaterialResponse?
}