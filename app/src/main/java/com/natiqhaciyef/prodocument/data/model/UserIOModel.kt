package com.natiqhaciyef.prodocument.data.model

import android.os.Parcelable
import com.natiqhaciyef.prodocument.data.base.IOModel
import kotlinx.parcelize.Parcelize

@Parcelize
class UserIOModel(
    var name: String,
    var email: String,
    var phoneNumber: String,
    var gender: String,
    var birthDate: String,
    var imageUrl: String,
    var password: String,
) : IOModel()

