package com.natiqhaciyef.prodocument.common.mapper

import com.natiqhaciyef.prodocument.common.helpers.hashPassword
import com.natiqhaciyef.prodocument.data.model.UserIOModel
import com.natiqhaciyef.prodocument.domain.model.mapped.MappedUserModel
import com.natiqhaciyef.prodocument.domain.model.mapped.MappedUserWithoutPasswordModel

// not hashable
fun UserIOModel.toMappedUserModel(hashed: Boolean): MappedUserModel {
    return if (hashed) MappedUserModel(
        name = this.name,
        email = this.email,
        phoneNumber = this.phoneNumber,
        gender = this.gender,
        birthDate = this.birthDate,
        imageUrl = this.imageUrl,
        password = this.password,
    )
    else
        MappedUserModel(
            name = this.name,
            email = this.email,
            phoneNumber = this.phoneNumber,
            gender = this.gender,
            birthDate = this.birthDate,
            imageUrl = this.imageUrl,
            password = hashPassword(this.password),
        )
}

fun MappedUserModel.toMappedUserWithoutPasswordModel(): MappedUserWithoutPasswordModel {
    return MappedUserWithoutPasswordModel(
        name = this.name,
        email = this.email,
        phoneNumber = this.phoneNumber,
        gender = this.gender,
        birthDate = this.birthDate,
        imageUrl = this.imageUrl,
    )
}

// first time hashable
fun MappedUserModel.toUserIOPassword(hashed: Boolean): UserIOModel {
    return if (hashed)
        UserIOModel(
            name = this.name,
            email = this.email,
            phoneNumber = this.phoneNumber,
            gender = this.gender,
            birthDate = this.birthDate,
            imageUrl = this.imageUrl,
            password = this.password,
        )
    else
        UserIOModel(
            name = this.name,
            email = this.email,
            phoneNumber = this.phoneNumber,
            gender = this.gender,
            birthDate = this.birthDate,
            imageUrl = this.imageUrl,
            password = hashPassword(this.password),
        )
}
