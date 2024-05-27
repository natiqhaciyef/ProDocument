package com.natiqhaciyef.domain.repository.impl

import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.data.network.response.SubscriptionResponse
import com.natiqhaciyef.data.source.SubscriptionDataSource
import com.natiqhaciyef.domain.repository.SubscriptionRepository

class SubscriptionRepositoryImpl(
    private val ds: SubscriptionDataSource
): SubscriptionRepository {

    override suspend fun getAllSubscriptionPlans(): NetworkResult<List<SubscriptionResponse>> =
        ds.getAllSubscriptionPlans()

    override suspend fun activatePickedPlan(planToken: String): NetworkResult<CRUDResponse> =
        ds.activatePickedPlan(planToken)
}