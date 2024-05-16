package com.natiqhaciyef.domain.usecase.user.local

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.common.objects.ResultCases
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.common.model.UIResult
import com.natiqhaciyef.common.model.mapped.MappedUserModel
import com.natiqhaciyef.data.mapper.toEntity
import com.natiqhaciyef.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class UpdateUserLocalUseCase @Inject constructor(
    userRepository: UserRepository
) : BaseUseCase<UserRepository, UIResult<MappedUserModel>, Nothing>(userRepository) {

    override fun run(data: UIResult<MappedUserModel>): Flow<Resource<Boolean>> = flow {
        emit(Resource.loading(null))
        repository.updateFromLocal(data.toEntity())
        Resource.success(true, ResultCases.UPDATE_SUCCESS)
    }
}