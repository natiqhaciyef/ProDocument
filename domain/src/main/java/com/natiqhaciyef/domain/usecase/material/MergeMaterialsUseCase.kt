package com.natiqhaciyef.domain.usecase.material

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toMapped
import com.natiqhaciyef.domain.mapper.toMaterialResponse
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import com.natiqhaciyef.domain.network.request.MergeRequest
import com.natiqhaciyef.domain.repository.MaterialRepository
import com.natiqhaciyef.domain.usecase.MATERIAL_LIST
import com.natiqhaciyef.domain.usecase.MATERIAL_TITLE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class MergeMaterialsUseCase @Inject constructor(
    materialRepository: MaterialRepository
) : BaseUseCase<
        MaterialRepository,
        Map<String, Any>,
        MappedMaterialModel>(materialRepository) {

    override fun operate(data: Map<String, Any>): Flow<Resource<MappedMaterialModel>> {
            val title = data[MATERIAL_TITLE].toString()
            val list = (data[MATERIAL_LIST] as List<MappedMaterialModel>).map { it.toMaterialResponse() }

            val request = MergeRequest(title = title, list = list)

            return handleFlowResult(
                networkResult = { repository.mergeMaterials(request) },
                operation = { Resource.success(it.toMapped()) }
            )
        }
}