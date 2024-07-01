package com.natiqhaciyef.domain.repository

import com.natiqhaciyef.core.base.repository.BaseRepository
import com.natiqhaciyef.domain.network.NetworkResult
import com.natiqhaciyef.domain.network.response.SubscriptionResponse

interface SubscriptionRepository: BaseRepository {

    suspend fun getAllSubscriptionPlans(): NetworkResult<List<SubscriptionResponse>>

    suspend fun getPickedPlan(email: String): NetworkResult<SubscriptionResponse>
}