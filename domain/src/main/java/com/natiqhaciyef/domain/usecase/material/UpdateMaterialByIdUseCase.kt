package com.natiqhaciyef.domain.usecase.material

import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toModel
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import com.natiqhaciyef.domain.repository.MaterialRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class UpdateMaterialByIdUseCase @Inject constructor(
    materialRepository: MaterialRepository
) : BaseUseCase<MaterialRepository, MappedMaterialModel, CRUDModel>(materialRepository) {

    override fun operate(data: MappedMaterialModel): Flow<Resource<CRUDModel>> {
        return handleFlowResult(
            networkResult = { repository.updateMaterialById(data) },
            operation = { Resource.success(it.toModel()) }
        )
    }
}