package com.natiqhaciyef.domain.usecase.app

import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.constants.UNKNOWN_ERROR
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.common.model.ProScanInfoModel
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class GetProscanDetailsUseCase @Inject constructor(
    appRepository: AppRepository
): BaseUseCase<AppRepository, Unit, ProScanInfoModel>(appRepository) {

    override fun invoke(): Flow<Resource<ProScanInfoModel>> = flow {
        emit(Resource.loading())

        when(val result = repository.getProscanDetails()){
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