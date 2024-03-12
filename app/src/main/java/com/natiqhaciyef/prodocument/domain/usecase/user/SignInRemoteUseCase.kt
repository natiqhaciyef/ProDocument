package com.natiqhaciyef.prodocument.domain.usecase.user

import com.natiqhaciyef.prodocument.common.model.Resource
import com.natiqhaciyef.prodocument.common.objects.ErrorMessages
import com.natiqhaciyef.prodocument.common.objects.ResultExceptions
import com.natiqhaciyef.prodocument.data.network.response.TokenResponse
import com.natiqhaciyef.prodocument.domain.base.BaseUseCase
import com.natiqhaciyef.prodocument.domain.base.UseCase
import com.natiqhaciyef.prodocument.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class SignInRemoteUseCase @Inject constructor(
    userRepository: UserRepository
) : BaseUseCase<UserRepository, Map<String, String>, TokenResponse>(userRepository) {

    override fun operate(data: Map<String, String>): Flow<Resource<TokenResponse>> = flow {
        val email = data["email"]
        val password = data["password"]

        emit(Resource.loading(null))
        if (email != null && password != null) {
            val result = repository.signIn(email, password)
            if (result != null) {
                if (result.uid != null) {
                    emit(Resource.success(result))
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