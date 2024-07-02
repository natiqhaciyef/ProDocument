package com.natiqhaciyef.data.repository

import com.natiqhaciyef.domain.network.NetworkResult
import com.natiqhaciyef.domain.network.response.SubscriptionResponse
import com.natiqhaciyef.data.source.SubscriptionDataSource
import com.natiqhaciyef.domain.repository.SubscriptionRepository
import javax.inject.Inject

class SubscriptionRepositoryImpl @Inject constructor(
    val ds: SubscriptionDataSource
): SubscriptionRepository {

    override suspend fun getAllSubscriptionPlans(): NetworkResult<List<SubscriptionResponse>> =
        ds.getAllSubscriptionPlans()

    override suspend fun getPickedPlan(email: String): NetworkResult<SubscriptionResponse> =
        ds.getPickedPlan(email)

}