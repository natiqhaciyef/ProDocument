package com.natiqhaciyef.domain.usecase.user.remote

import com.natiqhaciyef.common.mapper.toMapped
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.common.objects.ResultExceptions
import com.natiqhaciyef.domain.base.usecase.BaseUseCase
import com.natiqhaciyef.domain.base.usecase.UseCase
import com.natiqhaciyef.domain.repository.UserRepository
import com.natiqhaciyef.domain.usecase.USER_EMAIL
import com.natiqhaciyef.domain.usecase.USER_PASSWORD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class UpdateUserPasswordByEmailRemoteUseCase @Inject constructor(
    userRepository: UserRepository
) : BaseUseCase<UserRepository, Map<String, String>, MappedTokenModel>(userRepository) {

    override fun operate(data: Map<String, String>): Flow<Resource<MappedTokenModel>> = flow {
        emit(Resource.loading(null))
        val email = data[USER_EMAIL].toString()
        val password = data[USER_PASSWORD].toString()

        val result = repository.updateUserPasswordByEmail(email, password)

        result.onSuccess { value ->
            val mapped = value.toMapped()
            emit(Resource.success(mapped))
        }.onFailure { exception ->
            emit(Resource.error(
                msg = exception.message ?: ErrorMessages.UNKNOWN_ERROR,
                data = null,
                exception = ResultExceptions.CustomIOException(
                    msg = exception.message ?: ErrorMessages.UNKNOWN_ERROR,
                    errorCode = 500
                )
            ))
        }
    }
}