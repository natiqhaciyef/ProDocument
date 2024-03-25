package com.natiqhaciyef.domain.usecase.user

import com.natiqhaciyef.common.mapper.toMapped
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.common.objects.ResultExceptions
import com.natiqhaciyef.data.network.response.TokenResponse
import com.natiqhaciyef.domain.base.BaseUseCase
import com.natiqhaciyef.domain.base.UseCase
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
        val email = data[USER_EMAIL]
        val password = data[USER_PASSWORD]

        emit(Resource.loading(null))
        if (email != null && password != null) {
            val result = repository.signIn(email, password)
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

        } else {
            emit(
                Resource.error(
                    msg = ErrorMessages.WRONG_FILLED_FIELD,
                    data = null,
                    exception = ResultExceptions.FieldsNotFound()
                )
            )
        }
    }
}