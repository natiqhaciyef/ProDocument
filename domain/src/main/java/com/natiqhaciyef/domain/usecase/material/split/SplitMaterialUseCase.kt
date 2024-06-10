package com.natiqhaciyef.domain.usecase.material.split

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.data.network.request.SplitRequest
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.data.mapper.toMapped
import com.natiqhaciyef.data.mapper.toMaterialResponse
import com.natiqhaciyef.domain.repository.MaterialRepository
import com.natiqhaciyef.domain.usecase.MATERIAL_FIRST_LINE
import com.natiqhaciyef.domain.usecase.MATERIAL_LAST_LINE
import com.natiqhaciyef.domain.usecase.MATERIAL_MODEL
import com.natiqhaciyef.domain.usecase.MATERIAL_TITLE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class SplitMaterialUseCase @Inject constructor(
    materialRepository: MaterialRepository
) : BaseUseCase<MaterialRepository, Map<String, Any>, List<MappedMaterialModel>>(materialRepository) {

    override fun operate(data: Map<String, Any>): Flow<Resource<List<MappedMaterialModel>>> = flow {
        emit(Resource.loading(null))

        val title = data[MATERIAL_TITLE].toString()
        val material = (data[MATERIAL_MODEL] as MappedMaterialModel).toMaterialResponse()
        val firstLine = data[MATERIAL_FIRST_LINE].toString()
        val lastLine = data[MATERIAL_LAST_LINE].toString()
        val splitRequest = SplitRequest(
            title = title,
            material = material,
            firstLine = firstLine,
            lastLine = lastLine
        )

        when (val result = repository.splitMaterial(splitRequest)) {
            is NetworkResult.Success -> {
                if (result.data.isNotEmpty() && result.data.all { it.result?.resultCode in 200..299 && !it.title.isNullOrEmpty() }) {
                    val mapped = result.data.map { it.toMapped()!! }

                    emit(Resource.success(mapped))
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