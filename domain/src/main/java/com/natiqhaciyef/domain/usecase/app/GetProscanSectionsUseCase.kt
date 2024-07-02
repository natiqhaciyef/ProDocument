package com.natiqhaciyef.domain.usecase.app

import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.constants.UNKNOWN_ERROR
import com.natiqhaciyef.common.model.ProscanSectionModel
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.repository.AppRepository
import com.natiqhaciyef.domain.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class GetProscanSectionsUseCase @Inject constructor(
    appRepository: AppRepository
): BaseUseCase<AppRepository, Unit, List<ProscanSectionModel>>(appRepository) {

    override fun invoke(): Flow<Resource<List<ProscanSectionModel>>> = flow {
        emit(Resource.loading())

        when(val result = repository.getProscanSections()){
            is NetworkResult.Success -> {
                emit(Resource.success(result.data))
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