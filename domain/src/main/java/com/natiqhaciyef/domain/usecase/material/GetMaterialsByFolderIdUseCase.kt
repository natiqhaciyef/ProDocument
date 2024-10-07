package com.natiqhaciyef.domain.usecase.material

import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.LINE
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import com.natiqhaciyef.domain.mapper.toMapped
import com.natiqhaciyef.domain.repository.MaterialRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class GetMaterialsByFolderIdUseCase @Inject constructor(
    materialRepository: MaterialRepository
) : BaseUseCase<MaterialRepository, String, List<MappedMaterialModel>>(materialRepository) {

    override fun operate(data: String): Flow<Resource<List<MappedMaterialModel>>> =
        handleFlowResult(
            networkResult = { repository.getMaterialsByFolderId(data) },
            operation = { list ->
                Resource.success(list.map {
                    if (it.title != EMPTY_STRING)
                        it.toMapped()!!
                    else
                        it.copy(title = LINE).toMapped()!!
                })
            }
        )

}