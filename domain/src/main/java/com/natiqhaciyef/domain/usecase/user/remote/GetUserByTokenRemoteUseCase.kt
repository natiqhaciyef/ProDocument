package com.natiqhaciyef.domain.usecase.user.remote

import com.natiqhaciyef.common.constants.MAPPED_NULL_DATA
import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.common.model.mapped.MappedUserWithoutPasswordModel
import com.natiqhaciyef.common.constants.ResultExceptions
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.constants.TWO_HUNDRED
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.common.constants.UNKNOWN_ERROR
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.data.mapper.toMappedUserWithoutPassword
import com.natiqhaciyef.data.mapper.toUIResult
import com.natiqhaciyef.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class GetUserByTokenRemoteUseCase @Inject constructor(
    userRepository: UserRepository
) : BaseUseCase<UserRepository, Unit, MappedUserWithoutPasswordModel>(userRepository) {

    override fun invoke(): Flow<Resource<MappedUserWithoutPasswordModel>> = flow {
        emit(Resource.loading(null))

        when (val result = repository.getUser()) {
            is NetworkResult.Success -> {
                val model = result.data.toUIResult()

                if (model?.result?.resultCode in TWO_HUNDRED..TWO_HUNDRED_NINETY_NINE && model != null)
                    emit(Resource.success(data = model.data.toMappedUserWithoutPassword()))
                else
                    emit(
                        Resource.error(
                            msg = MAPPED_NULL_DATA,
                            data = null,
                            exception = ResultExceptions.CustomIOException(
                                msg = MAPPED_NULL_DATA,
                                errorCode = -ONE
                            )
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
                emit(
                    Resource.error(
                        msg = result.e.message ?: SOMETHING_WENT_WRONG,
                        data = null,
                        exception = Exception(result.e),
                        errorCode = -ONE
                    )
                )
            }

            else -> {}
        }
    }

}