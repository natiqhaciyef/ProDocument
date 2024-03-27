package com.natiqhaciyef.domain.usecase.material

import com.natiqhaciyef.common.mapper.toModel
import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.common.objects.ResultExceptions
import com.natiqhaciyef.domain.base.BaseUseCase
import com.natiqhaciyef.domain.base.UseCase
import com.natiqhaciyef.domain.repository.MaterialRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class RemoveMaterialByIdUseCase @Inject constructor(
    materialRepository: MaterialRepository
) : BaseUseCase<MaterialRepository, Map<String, String>, CRUDModel>(materialRepository) {

    override fun operate(data: Map<String, String>): Flow<Resource<CRUDModel>> = flow {
        emit(Resource.loading(null))
        val materialId = data["materialId"].toString()
        val materialToken = data["materialToken"].toString()

        val result =
            repository.removeMaterialById(materialId = materialId, materialToken = materialToken)

        if (result != null) {
            val crudModel = result.toModel()
            if (crudModel.resultCode in 200..299)
                emit(Resource.success(crudModel))
            else
                emit(Resource.error(
                    data = crudModel,
                    msg = "${crudModel.resultCode}: ${crudModel.message}",
                    exception = Exception(crudModel.message)
                ))
        } else {
            emit(
                Resource.error(
                    msg = ErrorMessages.SOMETHING_WENT_WRONG,
                    data = null,
                    exception = ResultExceptions.UnknownError()
                )
            )
        }
    }
}