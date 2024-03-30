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
class SignInRemoteUseCase @Inject constructor(
    userRepository: UserRepository
) : BaseUseCase<UserRepository, Map<String, String>, MappedTokenModel>(userRepository) {

    override fun operate(data: Map<String, String>): Flow<Resource<MappedTokenModel>> = flow {
        val email = data[USER_EMAIL].toString()
        val password = data[USER_PASSWORD].toString()

        emit(Resource.loading(null))
        val result = repository.signIn(email, password)

        result.onSuccess { value ->
            val mapped = value.toMapped()
            if (mapped.uid != null) {
                emit(Resource.success(mapped))
            } else {
                emit(
                    Resource.error(
                        msg = mapped.result?.message ?: ErrorMessages.UNKNOWN_ERROR,
                        data = null,
                        exception = ResultExceptions.CustomIOException(
                            msg = mapped.result?.message ?: ErrorMessages.UNKNOWN_ERROR,
                            errorCode = mapped.result?.resultCode ?: 500
                        )
                    )
                )
            }
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