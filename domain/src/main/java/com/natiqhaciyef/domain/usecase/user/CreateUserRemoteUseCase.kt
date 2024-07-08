package com.natiqhaciyef.domain.usecase.user

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedTokenModel
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.common.model.mapped.MappedUserModel
import com.natiqhaciyef.domain.mapper.toMapped
import com.natiqhaciyef.domain.repository.UserRepository
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class CreateUserRemoteUseCase @Inject constructor(
    userRepository: UserRepository
) : BaseUseCase<UserRepository, MappedUserModel, MappedTokenModel?>(userRepository) {

    override fun operate(data: MappedUserModel): Flow<Resource<MappedTokenModel?>>{
        return handleFlowResult(
            networkResult = { repository.createAccount(data) },
            operation = { Resource.success(it.toMapped()) }
        )
    }

}