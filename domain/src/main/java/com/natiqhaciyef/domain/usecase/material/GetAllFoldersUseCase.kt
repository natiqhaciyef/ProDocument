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
class GetAllFoldersUseCase @Inject constructor(
    materialRepository: MaterialRepository
) : BaseUseCase<MaterialRepository, Unit, List<MappedFolderModel>>(materialRepository) {

    override fun invoke(): Flow<Resource<List<MappedFolderModel>>> = handleFlowResult(
        networkResult = { repository.getAllFolders() },
        operation = { list -> Resource.success(list.map { it.toMapped() }) }
    )

}