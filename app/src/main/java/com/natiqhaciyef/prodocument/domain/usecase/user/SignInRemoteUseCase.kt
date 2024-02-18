package com.natiqhaciyef.prodocument.domain.usecase.user

import com.natiqhaciyef.prodocument.common.clazz.Resource
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
                emit(Resource.success(result))
            } else {
                emit(
                    Resource.error(
                        msg = ErrorMessages.TOKEN_REQUEST_FAILED,
                        data = null,
                        exception = ResultExceptions.TokenRequestFailed()
                    )
                )
            }

        } else {
            emit(
                Resource.error(
                    msg = ErrorMessages.EMPTY_FIELD,
                    data = null,
                    exception = ResultExceptions.FieldsNotFound()
                )
            )
        }
    }
}