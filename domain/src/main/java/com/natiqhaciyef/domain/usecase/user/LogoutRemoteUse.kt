package com.natiqhaciyef.domain.usecase.user

import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.constants.TWO_HUNDRED
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.common.constants.UNKNOWN_ERROR
import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toModel
import com.natiqhaciyef.domain.network.NetworkResult
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
        when (result) {
            is NetworkResult.Success -> {
                val model = result.data.toModel()

                if (model.resultCode in TWO_HUNDRED..TWO_HUNDRED_NINETY_NINE)
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
                        msg = result.message ?: UNKNOWN_ERROR,
                        data = null,
                        exception = Exception(result.message),
                        errorCode = result.code
                    )
                )
            }

            is NetworkResult.Exception -> {
                emit(Resource.error(
                    msg = result.e.message ?: SOMETHING_WENT_WRONG,
                    data = null,
                    exception = Exception(result.e),
                    errorCode = -ONE
                ))
            }
        }
    }
}