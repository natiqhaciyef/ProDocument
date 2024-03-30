package com.natiqhaciyef.domain.usecase.material

import com.natiqhaciyef.common.mapper.toModel
import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.common.objects.ResultExceptions
import com.natiqhaciyef.domain.base.usecase.BaseUseCase
import com.natiqhaciyef.domain.base.usecase.UseCase
import com.natiqhaciyef.domain.repository.MaterialRepository
import com.natiqhaciyef.domain.usecase.MATERIAL_ID
import com.natiqhaciyef.domain.usecase.MATERIAL_TOKEN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class RemoveMaterialByIdUseCase @Inject constructor(
    materialRepository: MaterialRepository
) : BaseUseCase<MaterialRepository, Map<String, String>, CRUDModel>(materialRepository) {

    override fun operate(data: Map<String, String>): Flow<Resource<CRUDModel>> = flow {
        emit(Resource.loading(null))
        val materialId = data[MATERIAL_ID].toString()
        val materialToken = data[MATERIAL_TOKEN].toString()

        val result =
            repository.removeMaterialById(materialId = materialId, materialToken = materialToken)

        result.onSuccess { value ->
            val crudModel = value.toModel()
            if (crudModel.resultCode in 200..299)
                emit(Resource.success(crudModel))
            else
                emit(
                    Resource.error(
                        data = crudModel,
                        msg = "${crudModel.resultCode}: ${crudModel.message}",
                        exception = Exception(crudModel.message)
                    )
                )
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