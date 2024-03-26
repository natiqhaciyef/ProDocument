package com.natiqhaciyef.domain.usecase.user.remote

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
) : BaseUseCase<UserRepository, String, CRUDModel?>(userRepository) {

    override fun operate(data: String): Flow<Resource<CRUDModel?>> = flow {
        emit(Resource.loading(null))
        val result = repository.getOtp(data)

        if (result != null) {
            val crudModel = result.toModel()
            if (crudModel.resultCode in 200..299)
                emit(Resource.success(data = crudModel))
            else
                emit(Resource.error(
                    data = crudModel,
                    msg = "${crudModel.resultCode}: ${crudModel.message}",
                    exception = Exception(crudModel.message)
                ))

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