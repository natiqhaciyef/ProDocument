package com.natiqhaciyef.domain.usecase.material

import com.natiqhaciyef.common.constants.LINE
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import com.natiqhaciyef.domain.mapper.toMapped
import com.natiqhaciyef.domain.repository.MaterialRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class GetAllMaterialsRemoteUseCase @Inject constructor(
    materialRepository: MaterialRepository
) : BaseUseCase<MaterialRepository, Unit, List<MappedMaterialModel>>(materialRepository) {

    override fun invoke(): Flow<Resource<List<MappedMaterialModel>>> {
        return handleFlowResult(
            networkResult = { repository.getAllMaterials() },
            operation = {
                Resource.success(it.materials.map {
                    it.toMapped() ?: it.copy(title = LINE).toMapped()!!
                })
            }
        )
    }
}