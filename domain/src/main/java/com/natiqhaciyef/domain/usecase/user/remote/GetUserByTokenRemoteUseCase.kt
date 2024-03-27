package com.natiqhaciyef.domain.usecase.user.remote

import com.natiqhaciyef.common.mapper.toUIResult
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.domain.base.BaseUseCase
import com.natiqhaciyef.domain.base.UseCase
import com.natiqhaciyef.common.model.UIResult
import com.natiqhaciyef.common.model.mapped.MappedUserModel
import com.natiqhaciyef.common.objects.ErrorMessages
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

        if (result != null) {
            val uiResult = result.toUIResult()
            if (uiResult?.result != null) {
                if (uiResult.result!!.resultCode in 200..299)
                    emit(Resource.success(uiResult))
                else
                    emit(
                        Resource.error(
                            data = uiResult,
                            msg = "${uiResult.result!!.resultCode}: ${uiResult.result!!.message}",
                            exception = Exception(uiResult.result?.message)
                        )
                    )
            } else {
                emit(
                    Resource.error(
                        msg = result.result?.message ?: ErrorMessages.UNKNOWN_ERROR,
                        data = null,
                        exception = ResultExceptions.CustomIOException(
                            msg = result.result?.message ?: ErrorMessages.UNKNOWN_ERROR,
                            errorCode = result.result?.resultCode ?: 500
                        )
                    )
                )
            }
        } else {
            emit(
                Resource.error(
                    msg = ErrorMessages.SOMETHING_WENT_WRONG,
                    data = null,
                    exception = ResultExceptions.UnknownError()
                )
            )
        }
    }

}