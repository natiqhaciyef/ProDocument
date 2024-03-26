package com.natiqhaciyef.domain.usecase.user.remote

import com.natiqhaciyef.common.mapper.toModel
import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.common.objects.ResultExceptions
import com.natiqhaciyef.domain.base.BaseUseCase
import com.natiqhaciyef.domain.base.UseCase
import com.natiqhaciyef.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class LogoutRemoteUse @Inject constructor(
    userRepository: UserRepository
): BaseUseCase<UserRepository, Nothing, CRUDModel>(userRepository) {

    override fun invoke(): Flow<Resource<CRUDModel>> = flow{
        emit(Resource.loading(null))

        val result = repository.logout()
        if (result != null){
            val uiModel = result.toModel()

            if (uiModel.resultCode in 200..299){
                emit(Resource.success(uiModel))
            }else{
                emit(Resource.error(
                    data = uiModel,
                    msg = "${uiModel.resultCode}: ${uiModel.message}",
                    exception = Exception(uiModel.message)
                ))
            }

        }else{
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