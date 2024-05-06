package com.natiqhaciyef.domain.usecase.material.watermark

import com.google.gson.Gson
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.data.network.request.WatermarkRequest
import com.natiqhaciyef.data.network.response.MaterialResponse
import com.natiqhaciyef.domain.base.usecase.BaseUseCase
import com.natiqhaciyef.domain.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toMappedModel
import com.natiqhaciyef.domain.mapper.toMaterialResponse
import com.natiqhaciyef.domain.repository.MaterialRepository
import com.natiqhaciyef.domain.usecase.MATERIAL_MODEL
import com.natiqhaciyef.domain.usecase.MATERIAL_TITLE
import com.natiqhaciyef.domain.usecase.MATERIAL_WATERMARK
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class WatermarkMaterialUseCase @Inject constructor(
    materialRepository: MaterialRepository
): BaseUseCase<MaterialRepository, Map<String, Any>, MappedMaterialModel>(materialRepository) {

    override fun operate(data: Map<String, Any>): Flow<Resource<MappedMaterialModel>> = flow{
        emit(Resource.loading(null))
        // fix the bug of mapped material model
        val title = data[MATERIAL_TITLE].toString()
        val mappedMaterialModel = (data[MATERIAL_MODEL] as MappedMaterialModel).toMaterialResponse()
        val watermark = data[MATERIAL_WATERMARK].toString()

        val watermarkRequest = WatermarkRequest(title, mappedMaterialModel, watermark)

        when(val result = repository.watermarkMaterial(watermarkRequest)){
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