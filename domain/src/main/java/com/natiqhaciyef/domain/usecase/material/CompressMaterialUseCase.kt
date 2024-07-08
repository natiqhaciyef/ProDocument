package com.natiqhaciyef.domain.usecase.material

import com.natiqhaciyef.common.model.Quality
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toMapped
import com.natiqhaciyef.domain.mapper.toMaterialResponse
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import com.natiqhaciyef.domain.network.request.CompressRequest
import com.natiqhaciyef.domain.repository.MaterialRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class CompressMaterialUseCase @Inject constructor(
    materialRepository: MaterialRepository
): BaseUseCase<MaterialRepository, MappedMaterialModel, MappedMaterialModel>(materialRepository) {

    override fun operate(data: MappedMaterialModel): Flow<Resource<MappedMaterialModel>>{
        val quality = data.copy().quality?.name ?: Quality.STANDARD.name
        val material = data.copy(quality = null).toMaterialResponse()
        val request = CompressRequest(material = material, quality = quality)

        return handleFlowResult(
            networkResult = { repository.compressMaterial(request) },
            operation = { Resource.success(it.toMapped()) }
        )
    }

}