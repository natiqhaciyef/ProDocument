package com.natiqhaciyef.domain.usecase.subscription

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedSubscriptionModel
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toMapped
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import com.natiqhaciyef.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class GetPickedPlanUseCase @Inject constructor(
    subscriptionRepository: SubscriptionRepository
) : BaseUseCase<SubscriptionRepository, String, MappedSubscriptionModel>(subscriptionRepository) {

    override fun operate(data: String): Flow<Resource<MappedSubscriptionModel>>{
        return handleFlowResult(
            networkResult = { repository.getPickedPlan(data) },
            operation = { Resource.success(it.toMapped()) }
        )
    }
}