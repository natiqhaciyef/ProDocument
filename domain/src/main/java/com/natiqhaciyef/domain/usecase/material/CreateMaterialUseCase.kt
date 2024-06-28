package com.natiqhaciyef.domain.usecase.material

import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.constants.TWO_HUNDRED
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.common.constants.UNKNOWN_ERROR
import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.data.mapper.toModel
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.repository.MaterialRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class CreateMaterialUseCase @Inject constructor(
    materialRepository: MaterialRepository
) : BaseUseCase<MaterialRepository, MappedMaterialModel, CRUDModel>(materialRepository) {

    override fun operate(data: MappedMaterialModel): Flow<Resource<CRUDModel>> = flow {
        emit(Resource.loading(null))

        when (val result = repository.createMaterial(data)) {
            is NetworkResult.Success -> {
                val model = result.data.toModel()

                if (model.resultCode in TWO_HUNDRED..TWO_HUNDRED_NINETY_NINE)
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
                        msg = result.message ?: UNKNOWN_ERROR,
                        data = null,
                        exception = Exception(result.message),
                        errorCode = result.code
                    )
                )
            }

            is NetworkResult.Exception -> {
                emit(Resource.error(
                    msg = result.e.message ?: SOMETHING_WENT_WRONG,
                    data = null,
                    exception = Exception(result.e),
                    errorCode = -ONE
                ))
            }
        }

    }
}