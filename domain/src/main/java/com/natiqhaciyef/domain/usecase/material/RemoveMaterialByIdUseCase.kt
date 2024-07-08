package com.natiqhaciyef.domain.usecase.material

import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toModel
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import com.natiqhaciyef.domain.repository.MaterialRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class RemoveMaterialByIdUseCase @Inject constructor(
    materialRepository: MaterialRepository
) : BaseUseCase<MaterialRepository, String, CRUDModel>(materialRepository) {

    override fun operate(data: String): Flow<Resource<CRUDModel>>{
        return handleFlowResult(
            networkResult = { repository.removeMaterialById(data) },
            operation = { Resource.success(it.toModel()) }
        )
    }
}