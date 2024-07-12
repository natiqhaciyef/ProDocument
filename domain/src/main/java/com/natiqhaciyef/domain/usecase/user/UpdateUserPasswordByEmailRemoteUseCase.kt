package com.natiqhaciyef.domain.usecase.user

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toMapped
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import com.natiqhaciyef.domain.repository.UserRepository
import com.natiqhaciyef.domain.usecase.USER_EMAIL
import com.natiqhaciyef.domain.usecase.USER_PASSWORD
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class UpdateUserPasswordByEmailRemoteUseCase @Inject constructor(
    userRepository: UserRepository
) : BaseUseCase<UserRepository, Map<String, String>, MappedTokenModel>(userRepository) {

    override fun operate(data: Map<String, String>): Flow<Resource<MappedTokenModel>>{
        val email = data[USER_EMAIL].toString()
        val password = data[USER_PASSWORD].toString()

        return handleFlowResult(
            networkResult = { repository.updateUserPasswordByEmail(email, password) },
            operation = { Resource.success(it.toMapped()) }
        )
    }
}