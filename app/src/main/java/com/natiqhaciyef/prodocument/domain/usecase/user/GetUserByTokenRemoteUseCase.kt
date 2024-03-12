package com.natiqhaciyef.prodocument.domain.usecase.user

import com.natiqhaciyef.prodocument.common.mapper.toUIResult
import com.natiqhaciyef.prodocument.common.model.Resource
import com.natiqhaciyef.prodocument.common.objects.ErrorMessages
import com.natiqhaciyef.prodocument.domain.base.BaseUseCase
import com.natiqhaciyef.prodocument.common.objects.ResultExceptions
import com.natiqhaciyef.prodocument.domain.base.UseCase
import com.natiqhaciyef.prodocument.domain.model.UIResult
import com.natiqhaciyef.prodocument.domain.model.mapped.MappedUserModel
import com.natiqhaciyef.prodocument.domain.repository.UserRepository
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
            if (uiResult != null){
                emit(Resource.success(uiResult))
            }else{
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