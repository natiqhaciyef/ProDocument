package com.natiqhaciyef.domain.usecase.user

import com.natiqhaciyef.common.mapper.toMapped
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.mapper.toUser
import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.domain.base.BaseUseCase
import com.natiqhaciyef.domain.base.UseCase
import com.natiqhaciyef.common.model.mapped.MappedUserModel
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.common.objects.ResultExceptions
import com.natiqhaciyef.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class CreateUserRemoteUseCase @Inject constructor(
    userRepository: UserRepository
) : BaseUseCase<UserRepository, MappedUserModel, MappedTokenModel?>(userRepository) {

    override fun operate(data: MappedUserModel): Flow<Resource<MappedTokenModel?>> = flow {
        emit(Resource.loading(null))
        val result = repository.createAccount(data.toUser(hashed = false))

        if (result != null) {
            if (result.uid != null) {
                emit(Resource.success(result.toMapped()))
            } else {
                emit(
                    Resource.error(
                        msg = result.result?.message ?: ErrorMessages.UNKNOWN_ERROR,
                        data = null,
                        exception = ResultExceptions.CustomIOException(
                            msg = result.result?.message ?: ErrorMessages.UNKNOWN_ERROR,
                            errorCode = result.result?.resultCode ?: 500
                        )
                    )
                )
            }

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