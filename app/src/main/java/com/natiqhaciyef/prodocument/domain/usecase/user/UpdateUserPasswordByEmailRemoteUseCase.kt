package com.natiqhaciyef.prodocument.domain.usecase.user

import com.natiqhaciyef.prodocument.common.clazz.Resource
import com.natiqhaciyef.prodocument.common.objects.ErrorMessages
import com.natiqhaciyef.prodocument.common.objects.ResultExceptions
import com.natiqhaciyef.prodocument.data.network.response.CRUDResponse
import com.natiqhaciyef.prodocument.data.network.response.TokenResponse
import com.natiqhaciyef.prodocument.domain.base.BaseUseCase
import com.natiqhaciyef.prodocument.domain.base.UseCase
import com.natiqhaciyef.prodocument.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class UpdateUserPasswordByEmailRemoteUseCase @Inject constructor(
    userRepository: UserRepository
) : BaseUseCase<UserRepository, Map<String, String>, CRUDResponse>(userRepository) {

    override fun operate(data: Map<String, String>): Flow<Resource<CRUDResponse>> = flow {
        emit(Resource.loading(null))
        val email = data["email"]
        val password = data["password"]

        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            val result = repository.updateUserPasswordByEmail(email, password)

            if (result != null) {
                emit(Resource.success(result))
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