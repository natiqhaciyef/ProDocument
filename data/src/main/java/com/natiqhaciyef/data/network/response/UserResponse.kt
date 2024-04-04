package com.natiqhaciyef.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.natiqhaciyef.data.base.BaseNetworkModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(
    @SerializedName("full_name")
    var fullName: String,
    @SerializedName("phone_number")
    var phoneNumber: String,
    @SerializedName("gender")
    var gender: String,
    @SerializedName("dob")
    var dateOfBirth: String,
    @SerializedName("image_url")
    var imageUrl: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("publish_date")
    var publishDate: String,
    @SerializedName("result")
    override var result: CRUDResponse?
) : BaseNetworkModel, Parcelable

// data = split as a parameters of UserModel
