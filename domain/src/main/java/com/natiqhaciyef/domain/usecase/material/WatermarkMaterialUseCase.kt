package com.natiqhaciyef.domain.usecase.material

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toMapped
import com.natiqhaciyef.domain.mapper.toMaterialResponse
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import com.natiqhaciyef.domain.network.request.WatermarkRequest
import com.natiqhaciyef.domain.repository.MaterialRepository
import com.natiqhaciyef.domain.usecase.MATERIAL_MODEL
import com.natiqhaciyef.domain.usecase.MATERIAL_TITLE
import com.natiqhaciyef.domain.usecase.MATERIAL_WATERMARK
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class WatermarkMaterialUseCase @Inject constructor(
    materialRepository: MaterialRepository
): BaseUseCase<MaterialRepository, Map<String, Any>, MappedMaterialModel>(materialRepository) {

    override fun operate(data: Map<String, Any>): Flow<Resource<MappedMaterialModel>> {
        // fix the bug of mapped material model
        val title = data[MATERIAL_TITLE].toString()
        val mappedMaterialModel = (data[MATERIAL_MODEL] as MappedMaterialModel).toMaterialResponse()
        val watermark = data[MATERIAL_WATERMARK].toString()

        val watermarkRequest = WatermarkRequest(title, mappedMaterialModel, watermark)

        return handleFlowResult(
            networkResult = { repository.watermarkMaterial(watermarkRequest) },
            operation = { Resource.success(it.toMapped()) }
        )
    }
}