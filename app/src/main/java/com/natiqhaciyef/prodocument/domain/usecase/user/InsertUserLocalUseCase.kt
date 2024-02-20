package com.natiqhaciyef.prodocument.domain.usecase.user

import com.natiqhaciyef.prodocument.common.clazz.Resource
import com.natiqhaciyef.prodocument.common.mapper.toEntity
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
class InsertUserLocalUseCase @Inject constructor(
    userRepository: UserRepository
) : BaseUseCase<UserRepository, UIResult<MappedUserModel>, Nothing>(userRepository) {

    override fun run(data: UIResult<MappedUserModel>): Flow<Resource<Boolean>> = flow {
        emit(Resource.loading(null))
        repository.insertToLocal(data.toEntity())
        emit(
            Resource.success(
                data = true,
                message = ResultCases.INSERT_SUCCESS
            )
        )
    }
}