package com.natiqhaciyef.domain.usecase.user

import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.constants.UNKNOWN_ERROR
import com.natiqhaciyef.common.model.MappedGraphDetailModel
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toMappedList
import com.natiqhaciyef.domain.network.NetworkResult
import com.natiqhaciyef.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class GetUserStaticsUseCase @Inject constructor(
    userRepository: UserRepository
): BaseUseCase<UserRepository, Unit, List<MappedGraphDetailModel>>(userRepository) {

    override fun invoke(): Flow<Resource<List<MappedGraphDetailModel>>> = flow {
        emit(Resource.loading())

        when(val result = repository.getUserStatics()){
            is NetworkResult.Success -> {
                val mapped = result.data.toMappedList()
                emit(Resource.success(mapped))
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