package com.natiqhaciyef.data.source

import com.natiqhaciyef.core.base.mock.generateMockerClass
import com.natiqhaciyef.data.mock.subscription.ActivatePickedPlanMockGenerator
import com.natiqhaciyef.data.mock.subscription.GetAllSubscriptionPlansMockGenerator
import com.natiqhaciyef.data.network.LoadType
import com.natiqhaciyef.data.network.handleNetworkResponse
import com.natiqhaciyef.data.network.manager.TokenManager
import com.natiqhaciyef.data.network.service.SubscriptionService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SubscriptionDataSource @Inject constructor(
    private val service: SubscriptionService,
    private val manager: TokenManager
) {

    suspend fun getAllSubscriptionPlans() = withContext(Dispatchers.IO){
        val requestHeader = manager.generateToken()
        val mock = generateMockerClass(GetAllSubscriptionPlansMockGenerator::class, Unit)
            .getMock(Unit) { null }

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.getAllSubscriptionPlans(token = requestHeader)
        }
    }

    suspend fun activatePickedPlan(planToken: String) = withContext(Dispatchers.IO){
        val requestHeader = manager.generateToken()
        val mock = generateMockerClass(ActivatePickedPlanMockGenerator::class, planToken)
            .getMock(ActivatePickedPlanMockGenerator.customRequest) { null }

        handleNetworkResponse(mock = mock, handlingType = LoadType.MOCK) {
            service.activatePickedPlan(token = requestHeader, pickedPlanToken = planToken)
        }
    }
}