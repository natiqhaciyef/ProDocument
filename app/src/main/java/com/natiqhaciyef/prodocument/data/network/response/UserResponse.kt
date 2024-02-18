package com.natiqhaciyef.prodocument.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.natiqhaciyef.prodocument.data.base.BaseNetworkModel
import com.natiqhaciyef.prodocument.data.base.IOModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(
    @SerializedName("id")
    override var id: Int,
    @SerializedName("full_name")
    var fullName: String,
    @SerializedName("phone_number")
    var phoneNumber: String,
    @SerializedName("gender")
    var gender: String,
    @SerializedName("date_of_birth")
    var dateOfBirth: String,
    @SerializedName("image_url")
    var imageUrl: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("publish_date")
    override var publishDate: String,
    @SerializedName("result")
    override var resultCode: CRUDResponse
) : IOModel(), BaseNetworkModel, Parcelable

// data = split as a parameters of UserModel
