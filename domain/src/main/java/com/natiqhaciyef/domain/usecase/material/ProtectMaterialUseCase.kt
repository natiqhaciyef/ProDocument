package com.natiqhaciyef.domain.usecase.material

import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.constants.TWO_HUNDRED
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.common.constants.UNKNOWN_ERROR
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toMapped
import com.natiqhaciyef.domain.mapper.toMaterialResponse
import com.natiqhaciyef.domain.network.NetworkResult
import com.natiqhaciyef.domain.network.request.ProtectRequest
import com.natiqhaciyef.domain.repository.MaterialRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class ProtectMaterialUseCase @Inject constructor(
    materialRepository: MaterialRepository
): BaseUseCase<MaterialRepository, MappedMaterialModel, MappedMaterialModel>(materialRepository) {

    override fun operate(data: MappedMaterialModel): Flow<Resource<MappedMaterialModel>> = flow{
        emit(Resource.loading(null))

        val key = data.protectionKey
        val material = data.copy(isProtected = false, protectionKey = null).toMaterialResponse()

        // check key nullability
        val request = ProtectRequest(
            material = material,
            key = key ?: "Protected"
        )

        when (val result = repository.protectMaterial(request)) {
            is NetworkResult.Success -> {
                val mapped = result.data.toMapped()
                if (mapped != null && mapped.result?.resultCode in TWO_HUNDRED..TWO_HUNDRED_NINETY_NINE) {
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
                        msg = result.message ?: UNKNOWN_ERROR,
                        data = null,
                        exception = Exception(result.message),
                        errorCode = result.code
                    )
                )
            }

            is NetworkResult.Exception -> {
                emit(
                    Resource.error(
                        msg = result.e.message ?: SOMETHING_WENT_WRONG,
                        data = null,
                        exception = Exception(result.e),
                        errorCode = -ONE
                    )
                )
            }

            else -> {}
        }

    }
}