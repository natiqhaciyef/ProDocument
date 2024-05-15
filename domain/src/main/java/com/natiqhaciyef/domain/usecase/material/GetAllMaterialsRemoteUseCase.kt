package com.natiqhaciyef.domain.usecase.material

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.common.objects.ResultExceptions
import com.natiqhaciyef.data.mapper.toUIResult
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.domain.repository.MaterialRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class GetAllMaterialsRemoteUseCase @Inject constructor(
    materialRepository: MaterialRepository
) : BaseUseCase<MaterialRepository, Unit, List<MappedMaterialModel>>(materialRepository) {

    override fun invoke(): Flow<Resource<List<MappedMaterialModel>>> = flow {
        emit(Resource.loading(null))

        when (val result = repository.getAllMaterials()) {
            is NetworkResult.Success -> {
                val model = result.data.toUIResult()

                if (model?.result?.resultCode in 200..299 && model != null)
                    emit(Resource.success(data = model.data))
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