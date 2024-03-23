package com.natiqhaciyef.domain.usecase.material

import com.natiqhaciyef.common.helpers.toMappedMaterial
import com.natiqhaciyef.common.mapper.toMaterial
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
class CreateMaterialByTokenUseCase @Inject constructor(
    materialRepository: MaterialRepository
) : BaseUseCase<MaterialRepository, Map<String, String>, CRUDModel>(materialRepository) {

    override fun operate(data: Map<String, String>): Flow<Resource<CRUDModel>> = flow {
        emit(Resource.loading(null))

        val token = data["materialToken"].toString()
        val materialModel = data["mappedMaterialModel"]
            .toString()
            .toMappedMaterial()
            .toMaterial()

        val result = repository.createMaterialByToken(materialModel, token)
        if (result != null) {
            emit(Resource.success(data = result.toModel()))
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