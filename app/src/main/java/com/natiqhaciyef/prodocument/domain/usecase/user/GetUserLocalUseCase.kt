package com.natiqhaciyef.prodocument.domain.usecase.user

import com.natiqhaciyef.prodocument.common.clazz.Resource
import com.natiqhaciyef.prodocument.common.objects.ErrorMessages
import com.natiqhaciyef.prodocument.domain.base.BaseUseCase
import com.natiqhaciyef.prodocument.common.objects.ResultExceptions
import com.natiqhaciyef.prodocument.domain.base.ResultCases
import com.natiqhaciyef.prodocument.domain.base.UseCase
import com.natiqhaciyef.prodocument.domain.model.UIResult
import com.natiqhaciyef.prodocument.domain.model.mapped.MappedUserModel
import com.natiqhaciyef.prodocument.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class GetUserLocalUseCase @Inject constructor(
    userRepository: UserRepository
) : BaseUseCase<UserRepository, Nothing, MappedUserModel>(userRepository) {

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