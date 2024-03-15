package com.natiqhaciyef.domain.usecase.user

import com.natiqhaciyef.common.mapper.toModel
import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.common.objects.ResultExceptions
import com.natiqhaciyef.data.network.response.CRUDResponse
import com.natiqhaciyef.domain.base.BaseUseCase
import com.natiqhaciyef.domain.base.UseCase
import com.natiqhaciyef.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class UpdateUserPasswordByEmailRemoteUseCase @Inject constructor(
    userRepository: UserRepository
) : BaseUseCase<UserRepository, Map<String, String>, CRUDModel>(userRepository) {

    override fun operate(data: Map<String, String>): Flow<Resource<CRUDModel>> = flow {
        emit(Resource.loading(null))
        val email = data["email"]
        val password = data["password"]

        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            val result = repository.updateUserPasswordByEmail(email, password)

            if (result != null) {
                emit(Resource.success(result.toModel()))
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