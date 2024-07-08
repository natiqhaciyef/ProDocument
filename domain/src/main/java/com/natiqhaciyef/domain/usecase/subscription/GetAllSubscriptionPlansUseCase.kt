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
class GetAllSubscriptionPlansUseCase @Inject constructor(
    subscriptionRepository: SubscriptionRepository
) : BaseUseCase<SubscriptionRepository, Unit, List<MappedSubscriptionModel>>(subscriptionRepository) {

    override fun invoke(): Flow<Resource<List<MappedSubscriptionModel>>> {
        return handleFlowResult(
            networkResult = { repository.getAllSubscriptionPlans() },
            operation = { Resource.success(it.map { it.toMapped() }) }
        )
    }
}