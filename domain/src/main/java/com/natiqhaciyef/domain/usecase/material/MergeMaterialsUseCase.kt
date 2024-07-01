package com.natiqhaciyef.domain.usecase.material

import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.constants.TWO_HUNDRED
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.common.constants.UNKNOWN_ERROR
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toMapped
import com.natiqhaciyef.domain.mapper.toMaterialResponse
import com.natiqhaciyef.domain.network.NetworkResult
import com.natiqhaciyef.domain.network.request.MergeRequest
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