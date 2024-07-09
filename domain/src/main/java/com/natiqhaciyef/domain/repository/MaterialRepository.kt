package com.natiqhaciyef.domain.repository

import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.repository.BaseRepository
import com.natiqhaciyef.core.base.network.NetworkResult
import com.natiqhaciyef.domain.network.request.CompressRequest
import com.natiqhaciyef.domain.network.request.ESignRequest
import com.natiqhaciyef.domain.network.request.MergeRequest
import com.natiqhaciyef.domain.network.request.ProtectRequest
import com.natiqhaciyef.domain.network.request.SplitRequest
import com.natiqhaciyef.domain.network.request.WatermarkRequest
import com.natiqhaciyef.domain.network.response.ListMaterialResponse
import com.natiqhaciyef.domain.network.response.MaterialResponse

interface MaterialRepository : BaseRepository {

    suspend fun getAllMaterials(): NetworkResult<ListMaterialResponse>

    suspend fun getMaterialById(materialId: String): NetworkResult<MaterialResponse>

    suspend fun createMaterial(materialModel: MappedMaterialModel): NetworkResult<CRUDResponse>

    suspend fun removeMaterialById(materialId: String): NetworkResult<CRUDResponse>

    suspend fun updateMaterialById(materialModel: MappedMaterialModel): NetworkResult<CRUDResponse>

    suspend fun mergeMaterials(data: MergeRequest): NetworkResult<MaterialResponse>

    suspend fun watermarkMaterial(data: WatermarkRequest): NetworkResult<MaterialResponse>

    suspend fun splitMaterial(data: SplitRequest): NetworkResult<List<MaterialResponse>>

    suspend fun protectMaterial(data: ProtectRequest): NetworkResult<MaterialResponse>

    suspend fun compressMaterial(data: CompressRequest): NetworkResult<MaterialResponse>

    suspend fun eSignMaterial(data: ESignRequest): NetworkResult<MaterialResponse>
}