package com.natiqhaciyef.domain.usecase.material.merge

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.data.network.request.MergeRequest
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.data.mapper.toMappedModel
import com.natiqhaciyef.data.mapper.toMaterialResponse
import com.natiqhaciyef.domain.repository.MaterialRepository
import com.natiqhaciyef.domain.usecase.MATERIAL_LIST
import com.natiqhaciyef.domain.usecase.MATERIAL_TITLE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class MergeMaterialsUseCase @Inject constructor(
    materialRepository: MaterialRepository
) : BaseUseCase<
        MaterialRepository,
        Map<String, Any>,
        MappedMaterialModel>(materialRepository) {

    override fun operate(data: Map<String, Any>): Flow<Resource<MappedMaterialModel>> =
        flow {
            emit(Resource.loading(null))

            val title = data[MATERIAL_TITLE].toString()
            val list = (data[MATERIAL_LIST] as List<MappedMaterialModel>).map { it.toMaterialResponse() }

            val request = MergeRequest(title = title, list = list)

            when (val result = repository.mergeMaterials(request)) {
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