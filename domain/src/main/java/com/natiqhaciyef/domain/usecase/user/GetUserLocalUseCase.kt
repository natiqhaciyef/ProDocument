package com.natiqhaciyef.domain.usecase.user

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
class GetUserLocalUseCase @Inject constructor(
    userRepository: UserRepository
) : BaseUseCase<UserRepository, Nothing, List<UIResult<MappedUserModel>>>(userRepository) {

    override operator fun invoke(): Flow<Resource<List<UIResult<MappedUserModel>>>> = flow {
        emit(Resource.loading(null))

        val result = repository.getUserFromLocal()
        if (result != null) {
            emit(Resource.success(result))
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