package com.natiqhaciyef.domain.usecase.material

import com.natiqhaciyef.common.mapper.toUIResult
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.domain.base.usecase.BaseUseCase
import com.natiqhaciyef.domain.base.usecase.UseCase
import com.natiqhaciyef.common.model.UIResult
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.common.objects.ResultExceptions
import com.natiqhaciyef.domain.repository.MaterialRepository
import com.natiqhaciyef.domain.usecase.MATERIAL_ID
import com.natiqhaciyef.domain.usecase.MATERIAL_TOKEN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class GetMaterialByIdRemoteUseCase @Inject constructor(
    materialRepository: MaterialRepository
) : BaseUseCase<MaterialRepository, Map<String, String>, UIResult<MappedMaterialModel>>(
    materialRepository
) {

    override fun operate(data: Map<String, String>): Flow<Resource<UIResult<MappedMaterialModel>>> =
        flow {
            emit(Resource.loading(null))
            val materialId = data[MATERIAL_ID].toString()
            val token = data[MATERIAL_TOKEN].toString()


            val result = repository.getMaterialById(materialId = materialId, token = token)
            result.onSuccess { value ->
                val uiResult = value.toUIResult()

                if (uiResult != null) {
                    emit(Resource.success(uiResult))
                } else {
                    emit(
                        Resource.error(
                            msg = ErrorMessages.MAPPED_NULL_DATA,
                            data = null,
                            exception = ResultExceptions.CustomIOException(
                                msg = ErrorMessages.MAPPED_NULL_DATA,
                                errorCode = -1
                            )
                        )
                    )
                }
            }.onFailure { exception ->
                emit(
                    Resource.error(
                        msg = exception.message ?: ErrorMessages.UNKNOWN_ERROR,
                        data = null,
                        exception = ResultExceptions.CustomIOException(
                            msg = exception.message ?: ErrorMessages.UNKNOWN_ERROR,
                            errorCode = 500
                        )
                    )
                )
            }

        }
}