package com.natiqhaciyef.data.mock.subscription

import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.data.network.response.SubscriptionResponse

object SubscriptionMockManager {
    private val subscriptionList = mutableListOf(
        SubscriptionResponse(
            title = "Unit",
            price = 0.99,
            perTime = ONE,
            timeType = "day",
            description = "no description",
            features = listOf("Nothing", "Empty", "Unit", "Void"),
            expireDate = "0.00.0",
            backgroundColor = "blue",
            size = 256,
            sizeType = "MB",
            token = "subscriptionToken-2"
        ),
        SubscriptionResponse(
            title = "Empty",
            price = 1.99,
            perTime = ONE,
            timeType = "day",
            description = "no description",
            features = listOf("Nothing", "Empty", "Unit", "Void"),
            expireDate = "0.00.0",
            backgroundColor = "red",
            size = 512,
            sizeType = "MB",
            token = "subscriptionToken-1"
        ),
        SubscriptionResponse(
            title = "Week unit",
            price = 4.99,
            perTime = ONE,
            timeType = "week",
            description = "no description",
            features = listOf("Something", "Empty", "Unit", "Void", "No limit"),
            expireDate = "0.00.0",
            backgroundColor = "orange",
            size = 1024,
            sizeType = "MB",
            token = "subscriptionToken-3"
        )
    )

    fun getPickedPlan(email: String): SubscriptionResponse {
        return if (email == "steve@minecraft.com")
            subscriptionList.last()
        else
            subscriptionList.first()
    }

    fun getAllSubscriptions(): MutableList<SubscriptionResponse> {
        return subscriptionList
    }
}