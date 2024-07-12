package com.natiqhaciyef.domain.usecase.user

import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toModel
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import com.natiqhaciyef.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class SendOtpRemoteUseCase @Inject constructor(
    userRepository: UserRepository
) : BaseUseCase<UserRepository, String, CRUDModel?>(userRepository) {

    override fun operate(data: String): Flow<Resource<CRUDModel?>> {
        return handleFlowResult(
            networkResult = { repository.sendOtp(data) },
            operation = { Resource.success(it.toModel()) }
        )
    }
}