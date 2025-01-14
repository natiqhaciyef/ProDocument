package com.natiqhaciyef.domain.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.network.BaseNetworkModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(
    @SerializedName("fullName")
    var fullName: String,
    @SerializedName("phoneNumber")
    var phoneNumber: String,
    @SerializedName("gender")
    var gender: String,
    @SerializedName("dob")
    var dateOfBirth: String,
    @SerializedName("imageUrl")
    var imageUrl: String,
    @SerializedName("country")
    var country: String,
    @SerializedName("city")
    var city: String,
    @SerializedName("street")
    var street: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("publishDate")
    var publishDate: String,
    @SerializedName("isBiometricEnabled")
    var isBiometricEnabled: Boolean,
    @SerializedName("result")
    override var result: CRUDResponse?,
    @SerializedName("subscription")
    var subscription: SubscriptionResponse,
    @SerializedName("reports")
    var reports: GraphDetailsListResponse
) : BaseNetworkModel, Parcelable

// data = split as a parameters of UserModel
