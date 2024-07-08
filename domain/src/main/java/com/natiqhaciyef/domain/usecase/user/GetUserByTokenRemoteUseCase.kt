package com.natiqhaciyef.domain.usecase.user

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.common.model.mapped.MappedUserWithoutPasswordModel
import com.natiqhaciyef.domain.mapper.toMappedUserWithoutPassword
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import com.natiqhaciyef.domain.mapper.toMapped
import com.natiqhaciyef.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class GetUserByTokenRemoteUseCase @Inject constructor(
    userRepository: UserRepository
) : BaseUseCase<UserRepository, Unit, MappedUserWithoutPasswordModel>(userRepository) {

    override fun invoke(): Flow<Resource<MappedUserWithoutPasswordModel>> {
        return handleFlowResult(
            networkResult = { repository.getUser() },
            operation = { Resource.success(it.toMapped().toMappedUserWithoutPassword()) }
        )
    }

}