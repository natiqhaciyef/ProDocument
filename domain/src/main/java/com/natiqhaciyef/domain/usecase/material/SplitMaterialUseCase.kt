package com.natiqhaciyef.domain.usecase.material

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.constants.STAR
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toMapped
import com.natiqhaciyef.domain.mapper.toMaterialResponse
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import com.natiqhaciyef.domain.network.request.SplitRequest
import com.natiqhaciyef.domain.repository.MaterialRepository
import com.natiqhaciyef.domain.usecase.MATERIAL_FIRST_LINE
import com.natiqhaciyef.domain.usecase.MATERIAL_LAST_LINE
import com.natiqhaciyef.domain.usecase.MATERIAL_MODEL
import com.natiqhaciyef.domain.usecase.MATERIAL_TITLE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class SplitMaterialUseCase @Inject constructor(
    materialRepository: MaterialRepository
) : BaseUseCase<MaterialRepository, Map<String, Any>, List<MappedMaterialModel>>(materialRepository) {

    override fun operate(data: Map<String, Any>): Flow<Resource<List<MappedMaterialModel>>> {
        val title = data[MATERIAL_TITLE].toString()
        val material = (data[MATERIAL_MODEL] as MappedMaterialModel).toMaterialResponse()
        val firstLine = data[MATERIAL_FIRST_LINE].toString()
        val lastLine = data[MATERIAL_LAST_LINE].toString()
        val splitRequest = SplitRequest(
            title = title,
            material = material,
            firstLine = firstLine,
            lastLine = lastLine
        )

        return handleFlowResult(
            networkResult = { repository.splitMaterial(splitRequest) },
            operation = {
                Resource.success(it.map {
                    it.toMapped() ?: it.copy(title = STAR).toMapped()!!
                })
            }
        )
    }
}