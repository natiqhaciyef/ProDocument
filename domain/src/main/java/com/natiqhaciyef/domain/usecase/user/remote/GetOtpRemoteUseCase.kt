package com.natiqhaciyef.domain.usecase.user.remote

import com.natiqhaciyef.common.mapper.toModel
import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.common.objects.ResultExceptions
import com.natiqhaciyef.domain.base.usecase.BaseUseCase
import com.natiqhaciyef.domain.base.usecase.UseCase
import com.natiqhaciyef.domain.repository.UserRepository
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

        result.onSuccess { value ->
            val crudModel = value.toModel()
            if (crudModel.resultCode in 200..299)
                emit(Resource.success(data = crudModel))
            else
                emit(Resource.error(
                    data = crudModel,
                    msg = "${crudModel.resultCode}: ${crudModel.message}",
                    exception = Exception(crudModel.message)
                ))
        }.onFailure { exception ->
            emit(Resource.error(
                msg = exception.message ?: ErrorMessages.UNKNOWN_ERROR,
                data = null,
                exception = ResultExceptions.CustomIOException(
                    msg = exception.message ?: ErrorMessages.UNKNOWN_ERROR,
                    errorCode = 500
                )
            ))
        }

    }
}