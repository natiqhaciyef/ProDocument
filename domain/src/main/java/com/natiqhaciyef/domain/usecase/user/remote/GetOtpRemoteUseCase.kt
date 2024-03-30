package com.natiqhaciyef.domain.usecase.user.remote

import com.natiqhaciyef.common.mapper.toModel
import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.common.objects.ResultExceptions
import com.natiqhaciyef.data.network.NetworkResult
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

        when (result) {
            is NetworkResult.Success -> {
                val model = result.data.toModel()

                if (model.resultCode in 200..299)
                    emit(Resource.success(data = model))
                else
                    emit(
                        Resource.error(
                            data = model,
                            msg = "${model.resultCode}: ${model.message}",
                            exception = Exception(model.message)
                        )
                    )
            }

            is NetworkResult.Error -> {
                emit(
                    Resource.error(
                        msg = result.message ?: ErrorMessages.UNKNOWN_ERROR,
                        data = null,
                        exception = Exception(result.message),
                        errorCode = result.code
                    )
                )
            }

            is NetworkResult.Exception -> {
                emit(Resource.error(
                    msg = result.e.message ?: ErrorMessages.SOMETHING_WENT_WRONG,
                    data = null,
                    exception = Exception(result.e),
                    errorCode = -1
                ))
            }
        }
    }
}