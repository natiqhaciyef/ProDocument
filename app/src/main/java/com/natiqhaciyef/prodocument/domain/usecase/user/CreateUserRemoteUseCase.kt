package com.natiqhaciyef.prodocument.domain.usecase.user

import com.natiqhaciyef.prodocument.common.clazz.Resource
import com.natiqhaciyef.prodocument.common.mapper.toUser
import com.natiqhaciyef.prodocument.common.objects.ErrorMessages
import com.natiqhaciyef.prodocument.common.objects.ResultExceptions
import com.natiqhaciyef.prodocument.data.network.response.TokenResponse
import com.natiqhaciyef.prodocument.domain.base.BaseUseCase
import com.natiqhaciyef.prodocument.domain.base.UseCase
import com.natiqhaciyef.prodocument.domain.model.UIResult
import com.natiqhaciyef.prodocument.domain.model.mapped.MappedUserModel
import com.natiqhaciyef.prodocument.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class CreateUserRemoteUseCase @Inject constructor(
    userRepository: UserRepository
) : BaseUseCase<UserRepository, MappedUserModel, TokenResponse?>(userRepository) {

    override fun operate(data: MappedUserModel): Flow<Resource<TokenResponse?>> = flow {
        emit(Resource.loading(null))
        val result = repository.createAccount(data.toUser(hashed = false))

        if (result != null) {
            emit(Resource.success(result))
        } else {
            emit(
                Resource.error(
                    msg = ErrorMessages.NULL_PROPERTY,
                    data = null,
                    exception = ResultExceptions.TokenCreationFailed()
                )
            )
        }
    }

}