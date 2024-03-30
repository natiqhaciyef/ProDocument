package com.natiqhaciyef.domain.usecase.user.remote

import com.natiqhaciyef.common.mapper.toUIResult
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.domain.base.usecase.BaseUseCase
import com.natiqhaciyef.domain.base.usecase.UseCase
import com.natiqhaciyef.common.model.UIResult
import com.natiqhaciyef.common.model.mapped.MappedUserModel
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.common.objects.ErrorMessages.MAPPED_NULL_DATA
import com.natiqhaciyef.common.objects.ResultExceptions
import com.natiqhaciyef.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class GetUserByTokenRemoteUseCase @Inject constructor(
    userRepository: UserRepository
) : BaseUseCase<UserRepository, String, UIResult<MappedUserModel>?>(userRepository) {

    override fun operate(data: String): Flow<Resource<UIResult<MappedUserModel>?>> = flow {
        emit(Resource.loading(null))
        val result = repository.getUser(data)

        result.onSuccess { value ->
            val uiResult = value.toUIResult()

            if (uiResult != null) {
                if (uiResult.result!!.resultCode in 200..299) {
                    emit(Resource.success(uiResult))
                } else {
                    emit(
                        Resource.error(
                            data = uiResult,
                            msg = "${uiResult.result!!.resultCode}: ${uiResult.result!!.message}",
                            exception = Exception(uiResult.result?.message)
                        )
                    )
                }
            } else {
                emit(
                    Resource.error(
                        data = null,
                        msg = MAPPED_NULL_DATA,
                        exception = ResultExceptions.UnknownError(
                            msg = MAPPED_NULL_DATA,
                            errorCode = -1
                        )
                    )
                )
            }

        }.onFailure { exception ->
            emit(
                Resource.error(
                    msg = exception.message ?: ErrorMessages.UNKNOWN_ERROR,
                    data = null,
                    exception = ResultExceptions.CustomIOException(
                        msg = exception.message ?: ErrorMessages.UNKNOWN_ERROR,
                        errorCode = 500
                    )
                )
            )
        }
    }

}