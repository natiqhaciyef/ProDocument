package com.natiqhaciyef.domain.usecase.user

import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.constants.TWO_HUNDRED
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.common.constants.UNKNOWN_ERROR
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toMapped
import com.natiqhaciyef.domain.network.NetworkResult
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

        when (val result = repository.signIn(email, password)) {
            is NetworkResult.Success -> {
                val model = result.data.toMapped()

                if (model.result?.resultCode in TWO_HUNDRED..TWO_HUNDRED_NINETY_NINE)
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
                        msg = result.message ?: UNKNOWN_ERROR,
                        data = null,
                        exception = Exception(result.message),
                        errorCode = result.code
                    )
                )
            }

            is NetworkResult.Exception -> {
                emit(Resource.error(
                    msg = result.e.message ?: SOMETHING_WENT_WRONG,
                    data = null,
                    exception = Exception(result.e),
                    errorCode = -ONE
                ))
            }
        }
    }
}