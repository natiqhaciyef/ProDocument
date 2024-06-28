package com.natiqhaciyef.common.model.mapped

import android.os.Parcelable
import com.natiqhaciyef.common.constants.EMPTY_STRING
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
    var street: String = EMPTY_STRING
) : Parcelable
