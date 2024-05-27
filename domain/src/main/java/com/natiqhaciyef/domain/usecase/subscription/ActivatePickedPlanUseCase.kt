package com.natiqhaciyef.domain.usecase.subscription

import com.natiqhaciyef.common.model.CRUDModel
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.data.mapper.toModel
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class ActivatePickedPlanUseCase @Inject constructor(
    subscriptionRepository: SubscriptionRepository
): BaseUseCase<SubscriptionRepository, String, CRUDModel>(subscriptionRepository) {

    override fun operate(data: String): Flow<Resource<CRUDModel>> = flow{
        emit(Resource.loading(null))

        when(val result = repository.activatePickedPlan(data)){
            is NetworkResult.Success -> {
                val mapped = result.data.toModel()

                emit(Resource.success(mapped))
            }

            is NetworkResult.Error -> {
                emit(
                    Resource.error(
                        msg = result.message ?: ErrorMessages.UNKNOWN_ERROR,
                        data = null,
                        exception = Exception(result.message),
                        errorCode = result.code
                    )
                )
            }

            is NetworkResult.Exception -> {
                emit(
                    Resource.error(
                        msg = result.e.message ?: ErrorMessages.SOMETHING_WENT_WRONG,
                        data = null,
                        exception = Exception(result.e),
                        errorCode = -1
                    )
                )
            }
        }
    }
}