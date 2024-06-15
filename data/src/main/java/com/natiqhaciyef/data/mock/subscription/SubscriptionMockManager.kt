package com.natiqhaciyef.data.mock.subscription

import com.natiqhaciyef.data.network.response.SubscriptionResponse

object SubscriptionMockManager {
    private val subscriptionList = mutableListOf(
        SubscriptionResponse(
            title = "Empty",
            price = 1.99,
            perTime = 1,
            timeType = "day",
            description = "no description",
            features = listOf("Nothing", "Empty", "Unit", "Void"),
            expireDate = "0.00.0",
            backgroundColor = "red",
            token = "subscriptionToken-1"
        ),
        SubscriptionResponse(
            title = "Unit",
            price = 0.99,
            perTime = 1,
            timeType = "day",
            description = "no description",
            features = listOf("Nothing", "Empty", "Unit", "Void"),
            expireDate = "0.00.0",
            backgroundColor = "blue",
            token = "subscriptionToken-2"
        ),
        SubscriptionResponse(
            title = "Week unit",
            price = 4.99,
            perTime = 1,
            timeType = "week",
            description = "no description",
            features = listOf("Something", "Empty", "Unit", "Void", "No limit"),
            expireDate = "0.00.0",
            backgroundColor = "orange",
            token = "subscriptionToken-3"
        )
    )

    fun getAllSubscriptions(): MutableList<SubscriptionResponse> {
        return subscriptionList
    }
}