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
import com.natiqhaciyef.domain.usecase.USER_EMAIL
import com.natiqhaciyef.domain.usecase.USER_TOKEN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class GetOtpRemoteUseCase @Inject constructor(
    userRepository: UserRepository
) : BaseUseCase<UserRepository, Map<String, String>, CRUDModel?>(userRepository) {

    override fun operate(data: Map<String, String>): Flow<Resource<CRUDModel?>> = flow {
        emit(Resource.loading(null))
        val token = data[USER_TOKEN].toString()
        val email = data[USER_EMAIL].toString()
        val result = repository.getOtp(token, email)

        if (result != null) {
            emit(Resource.success(result.toModel()))
        } else {
            emit(
                Resource.error(
                    msg = ErrorMessages.SOMETHING_WENT_WRONG,
                    data = null,
                    exception = ResultExceptions.UnknownError()
                        .copy(msg = ErrorMessages.OTP_REQUEST_FAILED)
                )
            )
        }

    }
}