package com.natiqhaciyef.domain.usecase.material.protect

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.data.mapper.toMappedModel
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.data.network.request.ProtectRequest
import com.natiqhaciyef.domain.repository.MaterialRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class ProtectMaterialUseCase @Inject constructor(
    materialRepository: MaterialRepository
): BaseUseCase<MaterialRepository, MappedMaterialModel, MappedMaterialModel>(materialRepository) {

    override fun operate(data: MappedMaterialModel): Flow<Resource<MappedMaterialModel>> = flow{
        val key = data.protectionKey
        val material = data.copy(isProtected = false, protectionKey = null)

        // check key nullability
        val request = ProtectRequest(
            material = material,
            key = key ?: "Protected"
        )

        when (val result = repository.protectMaterial(request)) {
            is NetworkResult.Success -> {
                val mapped = result.data.toMappedModel()
                if (mapped != null && mapped.result?.resultCode in 200..299) {
                    emit(Resource.success(mapped))
                } else {
                    emit(
                        Resource.error(
                            data = mapped,
                            msg = "${mapped?.result?.resultCode}: ${mapped?.result?.message}",
                            exception = Exception(mapped?.result?.message)
                        )
                    )
                }
            }

            is NetworkResult.Error -> {
                emit(
                    Resource.error(
                        msg = result.message ?: ErrorMessages.UNKNOWN_ERROR,
                        data = null,
                        exception = Exception(result.message),
                        errorCode = result.code
                    )
                )
            }

            is NetworkResult.Exception -> {
                emit(
                    Resource.error(
                        msg = result.e.message ?: ErrorMessages.SOMETHING_WENT_WRONG,
                        data = null,
                        exception = Exception(result.e),
                        errorCode = -1
                    )
                )
            }

            else -> {}
        }

    }
}