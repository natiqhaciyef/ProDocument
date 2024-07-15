package com.natiqhaciyef.domain.usecase.user

import com.natiqhaciyef.common.model.mapped.MappedGraphDetailModel
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toMappedList
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import com.natiqhaciyef.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class GetUserStaticsUseCase @Inject constructor(
    userRepository: UserRepository
): BaseUseCase<UserRepository, Unit, List<MappedGraphDetailModel>>(userRepository) {

    override fun invoke(): Flow<Resource<List<MappedGraphDetailModel>>> {
        return handleFlowResult(
            networkResult = { repository.getUserStatics() },
            operation = { Resource.success(it.toMappedList()) }
        )
    }
}