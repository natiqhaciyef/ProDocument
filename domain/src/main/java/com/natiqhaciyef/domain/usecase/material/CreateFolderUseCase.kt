package com.natiqhaciyef.domain.usecase.material

import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import com.natiqhaciyef.domain.mapper.toModel
import com.natiqhaciyef.domain.network.request.FolderRequest
import com.natiqhaciyef.domain.repository.MaterialRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class CreateFolderUseCase @Inject constructor(
    materialRepository: MaterialRepository
) : BaseUseCase<MaterialRepository, FolderRequest, CRUDModel>(materialRepository) {

    override fun operate(data: FolderRequest): Flow<Resource<CRUDModel>> =
        handleFlowResult(
            networkResult = { repository.createFolder(data) },
            operation = { crud -> Resource.success(crud.toModel()) }
        )

}