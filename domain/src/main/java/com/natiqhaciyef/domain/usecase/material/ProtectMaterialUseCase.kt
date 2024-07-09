package com.natiqhaciyef.domain.usecase.material

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toMapped
import com.natiqhaciyef.domain.mapper.toMaterialResponse
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import com.natiqhaciyef.domain.network.request.ProtectRequest
import com.natiqhaciyef.domain.repository.MaterialRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class ProtectMaterialUseCase @Inject constructor(
    materialRepository: MaterialRepository
): BaseUseCase<MaterialRepository, MappedMaterialModel, MappedMaterialModel>(materialRepository) {

    override fun operate(data: MappedMaterialModel): Flow<Resource<MappedMaterialModel>> {
        val key = data.protectionKey
        val material = data.copy(isProtected = false, protectionKey = null).toMaterialResponse()

        val request = ProtectRequest(
            material = material,
            key = key ?: "Protected"
        )

        return handleFlowResult(
            networkResult = { repository.protectMaterial(request) },
            operation = { Resource.success(it.toMapped()) }
        )
    }
}