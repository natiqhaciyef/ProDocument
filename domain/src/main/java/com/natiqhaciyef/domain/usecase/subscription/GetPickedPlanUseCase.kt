package com.natiqhaciyef.domain.usecase.subscription

import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.constants.UNKNOWN_ERROR
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedSubscriptionModel
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.data.mapper.toMapped
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class GetPickedPlanUseCase @Inject constructor(
    subscriptionRepository: SubscriptionRepository
) : BaseUseCase<SubscriptionRepository, String, MappedSubscriptionModel>(subscriptionRepository) {

    override fun operate(data: String): Flow<Resource<MappedSubscriptionModel>> = flow{
        emit(Resource.loading())

        when(val result = repository.getPickedPlan(email = data)){
            is NetworkResult.Success -> {
                val mapped = result.data.toMapped()
                emit(Resource.success(mapped))
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
                emit(
                    Resource.error(
                        msg = result.e.message ?: SOMETHING_WENT_WRONG,
                        data = null,
                        exception = Exception(result.e),
                        errorCode = -1
                    )
                )
            }
        }
    }
}