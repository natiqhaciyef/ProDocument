package com.natiqhaciyef.domain.usecase.material

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.domain.mapper.toMapped
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import com.natiqhaciyef.domain.repository.MaterialRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class GetMaterialByIdRemoteUseCase @Inject constructor(
    materialRepository: MaterialRepository
) : BaseUseCase<MaterialRepository, String, MappedMaterialModel>(materialRepository) {

    override fun operate(data: String): Flow<Resource<MappedMaterialModel>>{
        return handleFlowResult(
            networkResult = { repository.getMaterialById(data) },
            operation = { Resource.success(it.toMapped()) }
        )
    }
}