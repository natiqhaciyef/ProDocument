package com.natiqhaciyef.domain.usecase.material

import com.natiqhaciyef.domain.mapper.toUIResult
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.domain.base.usecase.BaseUseCase
import com.natiqhaciyef.domain.base.usecase.UseCase
import com.natiqhaciyef.common.model.UIResult
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.common.objects.ResultExceptions
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.domain.mapper.toMappedModel
import com.natiqhaciyef.domain.repository.MaterialRepository
import com.natiqhaciyef.domain.usecase.MATERIAL_ID
import com.natiqhaciyef.domain.usecase.MATERIAL_TOKEN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class GetMaterialByIdRemoteUseCase @Inject constructor(
    materialRepository: MaterialRepository
) : BaseUseCase<MaterialRepository, Map<String, String>, MappedMaterialModel>(
    materialRepository
) {

    override fun operate(data: Map<String, String>): Flow<Resource<MappedMaterialModel>> =
        flow {
            emit(Resource.loading(null))
            val materialId = data[MATERIAL_ID].toString()
            val token = data[MATERIAL_TOKEN].toString()


            val result = repository.getMaterialById(materialId = materialId, token = token)
            when (result) {
                is NetworkResult.Success -> {
                    val model = result.data.toMappedModel()

                    if (model?.result?.resultCode in 200..299 && model != null)
                        emit(Resource.success(data = model))
                    else
                        emit(Resource.error(
                            msg = ErrorMessages.MAPPED_NULL_DATA,
                            data = null,
                            exception = ResultExceptions.CustomIOException(
                                msg = ErrorMessages.MAPPED_NULL_DATA,
                                errorCode = -1
                            )
                        ))
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
                    emit(Resource.error(
                        msg = result.e.message ?: ErrorMessages.SOMETHING_WENT_WRONG,
                        data = null,
                        exception = Exception(result.e),
                        errorCode = -1
                    ))
                }
            }

        }
}