package com.natiqhaciyef.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.natiqhaciyef.data.base.BaseNetworkModel
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
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("publishDate")
    var publishDate: String,
    @SerializedName("result")
    override var result: CRUDResponse?
) : BaseNetworkModel, Parcelable

// data = split as a parameters of UserModel
