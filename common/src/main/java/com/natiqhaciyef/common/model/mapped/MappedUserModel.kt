package com.natiqhaciyef.common.model.mapped

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.model.MappedGraphDetailModel
import kotlinx.parcelize.Parcelize


@Parcelize
data class MappedUserModel(
    var name: String,
    var email: String,
    var phoneNumber: String,
    var gender: String,
    var birthDate: String,
    var country: String = EMPTY_STRING,
    var city: String = EMPTY_STRING,
    var street: String = EMPTY_STRING,
    var imageUrl: String,
    var password: String,       // hashed
    var isBiometricEnabled: Boolean = false,
    var subscription: MappedSubscriptionModel,
    var reports: List<MappedGraphDetailModel> = listOf()
) : Parcelable

@Parcelize
data class MappedUserWithoutPasswordModel(
    var name: String,
    var email: String,
    var phoneNumber: String,
    var gender: String,
    var birthDate: String,
    var imageUrl: String,
    var country: String = EMPTY_STRING,
    var city: String = EMPTY_STRING,
    var street: String = EMPTY_STRING,
    var isBiometricEnabled: Boolean = false,
    var subscription: MappedSubscriptionModel,
    var reports: List<MappedGraphDetailModel> = listOf()
) : Parcelable
