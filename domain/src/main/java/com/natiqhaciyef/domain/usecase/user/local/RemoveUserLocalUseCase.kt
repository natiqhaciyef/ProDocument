package com.natiqhaciyef.domain.usecase.user.local

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.mapper.toEntity
import com.natiqhaciyef.domain.base.BaseUseCase
import com.natiqhaciyef.domain.base.ResultCases
import com.natiqhaciyef.domain.base.UseCase
import com.natiqhaciyef.common.model.UIResult
import com.natiqhaciyef.common.model.mapped.MappedUserModel
import com.natiqhaciyef.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class RemoveUserLocalUseCase @Inject constructor(
    userRepository: UserRepository
) : BaseUseCase<UserRepository, UIResult<MappedUserModel>, Nothing>(userRepository) {

    override fun run(data: UIResult<MappedUserModel>): Flow<Resource<Boolean>> = flow {
        emit(Resource.loading(null))
        repository.removeFromLocal(data.toEntity())
        Resource.success(true, ResultCases.REMOVE_SUCCESS)
    }
}