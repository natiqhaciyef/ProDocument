package com.natiqhaciyef.domain.usecase.user.remote

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.data.mapper.toMapped
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
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
        when (result) {
            is NetworkResult.Success -> {
                val model = result.data.toMapped()

                if (model.result?.resultCode in 200..299)
                    emit(Resource.success(data = model))
                else
                    emit(
                        Resource.error(
                            data = model,
                            msg = "${model.result?.resultCode}: ${model.result?.message}",
                            exception = Exception(model.result?.message)
                        )
                    )
            }

            is NetworkResult.Error -> {
                emit(
                    Resource.error(
                        msg = result.message ?: ErrorMessages.UNKNOWN_ERROR,
                        data = null,
                        exception = Exception(result.message),
                        errorCode = result.code
                    )
                )
            }

            is NetworkResult.Exception -> {
                emit(Resource.error(
                    msg = result.e.message ?: ErrorMessages.SOMETHING_WENT_WRONG,
                    data = null,
                    exception = Exception(result.e),
                    errorCode = -1
                ))
            }
        }
    }
}