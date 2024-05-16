package com.natiqhaciyef.domain.usecase.material

import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.data.mapper.toModel
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.repository.MaterialRepository
import com.natiqhaciyef.domain.usecase.MATERIAL_ID
import com.natiqhaciyef.domain.usecase.USER_EMAIL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class RemoveMaterialByIdUseCase @Inject constructor(
    materialRepository: MaterialRepository
) : BaseUseCase<MaterialRepository, String, CRUDModel>(materialRepository) {

    override fun operate(data: String): Flow<Resource<CRUDModel>> = flow {
        emit(Resource.loading(null))

        when (val result = repository.removeMaterialById(materialId = data)) {
            is NetworkResult.Success -> {
                val model = result.data.toModel()

                if (model.resultCode in 200..299)
                    emit(Resource.success(data = model))
                else
                    emit(
                        Resource.error(
                            data = model,
                            msg = "${model.resultCode}: ${model.message}",
                            exception = Exception(model.message)
                        )
                    )
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