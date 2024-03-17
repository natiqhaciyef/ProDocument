package com.natiqhaciyef.common.mapper

import com.google.firebase.firestore.DocumentSnapshot
import com.natiqhaciyef.common.helpers.hashPassword
import com.natiqhaciyef.common.model.mapped.MappedUserModel
import com.natiqhaciyef.common.model.mapped.MappedUserWithoutPasswordModel

fun DocumentSnapshot.toUserModelWithoutPassword(): MappedUserWithoutPasswordModel {
    return MappedUserWithoutPasswordModel(
        name = this["username"].toString(),
        email = this["email"].toString(),
        phoneNumber = this["phoneNumber"].toString(),
        gender = this["gender"].toString(),
        birthDate = this["birthDate"].toString(),
        imageUrl = this["imageUrl"].toString()
    )
}

fun MappedUserModel.toFirebaseHashMap(): HashMap<String, Any?> {
    val userMap = hashMapOf<String, Any?>()
    userMap["username"] = this.name
    userMap["email"] = this.email
    userMap["phoneNumber"] = this.phoneNumber
    userMap["gender"] = this.gender
    userMap["birthDate"] = this.birthDate
    userMap["imageUrl"] = this.imageUrl
    userMap["password"] = hashPassword(this.password)

    return userMap
}