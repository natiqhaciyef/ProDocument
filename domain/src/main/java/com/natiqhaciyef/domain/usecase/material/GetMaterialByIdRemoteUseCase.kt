package com.natiqhaciyef.domain.usecase.material

import com.natiqhaciyef.common.mapper.toUIResult
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.domain.base.BaseUseCase
import com.natiqhaciyef.domain.base.UseCase
import com.natiqhaciyef.common.model.UIResult
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.common.objects.ResultExceptions
import com.natiqhaciyef.domain.repository.MaterialRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class GetMaterialByIdRemoteUseCase @Inject constructor(
    materialRepository: MaterialRepository
): BaseUseCase<MaterialRepository, Map<String, String>, UIResult<MappedMaterialModel>>(materialRepository){

    override fun operate(data: Map<String, String>): Flow<Resource<UIResult<MappedMaterialModel>>> = flow{
        val materialId = data["materialId"]
        val token = data["token"]

        emit(Resource.loading(null))
        if (materialId != null && token != null) {
            val result = repository.getMaterialById(materialId = materialId, token = token)
            if (result != null) {
                val uiResult = result.toUIResult()

                if (uiResult != null) {
                    emit(Resource.success(uiResult))
                } else {
                    emit(
                        Resource.error(
                            msg = result.result?.message ?: ErrorMessages.UNKNOWN_ERROR,
                            data = null,
                            exception = ResultExceptions.CustomIOException(
                                msg = result.result?.message ?: ErrorMessages.UNKNOWN_ERROR,
                                errorCode = result.result?.resultCode ?: 500
                            )
                        )
                    )
                }

            } else {
                emit(
                    Resource.error(
                        msg = ErrorMessages.SOMETHING_WENT_WRONG,
                        data = null,
                        exception = ResultExceptions.UnknownError()
                    )
                )
            }

        } else {
            emit(
                Resource.error(
                    msg = ErrorMessages.DATA_NOT_FOUND,
                    data = null,
                    exception = ResultExceptions.FieldsNotFound()
                )
            )
        }
    }
}