package com.natiqhaciyef.domain.usecase.material

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedFolderModel
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import com.natiqhaciyef.domain.mapper.toMapped
import com.natiqhaciyef.domain.repository.MaterialRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class GetFolderByIdUseCase @Inject constructor(
    materialRepository: MaterialRepository
): BaseUseCase<MaterialRepository, String, MappedFolderModel>(materialRepository) {

    override fun operate(data: String): Flow<Resource<MappedFolderModel>> =
        handleFlowResult(
            networkResult = { repository.getFolderById(data) },
            operation = { result -> Resource.success(result.toMapped()) }
        )

}