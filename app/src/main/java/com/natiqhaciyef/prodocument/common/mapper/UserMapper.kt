package com.natiqhaciyef.prodocument.common.mapper

import com.google.gson.Gson
import com.natiqhaciyef.prodocument.common.helpers.hashPassword
import com.natiqhaciyef.prodocument.data.model.UserIOModel
import com.natiqhaciyef.prodocument.data.network.response.UserResponse
import com.natiqhaciyef.prodocument.domain.model.UIResult
import com.natiqhaciyef.prodocument.domain.model.mapped.MappedUserModel
import com.natiqhaciyef.prodocument.domain.model.mapped.MappedUserWithoutPasswordModel
import com.natiqhaciyef.prodocument.domain.repository.UserRepository

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

fun UserResponse.toUIResult(): UIResult<MappedUserModel>? {
    return if (this.data.isNotEmpty()) {
        val mappedUser =
            Gson().fromJson(this.data, UserIOModel::class.java).toMappedUserModel(hashed = true)
        UIResult(
            id = this.id,
            data = mappedUser,
            publishDate = this.publishDate
        )
    } else {
        null
    }
}

fun UIResult<MappedUserModel>.toResponse(): UserResponse? {
    val parsedUser = Gson().toJson(this.data, UserIOModel::class.java)

    return if (
        this.data.email.isNotEmpty()
        && this.data.name.isNotEmpty()
        && this.data.password.isNotEmpty()
        && this.data.phoneNumber.isNotEmpty()
        && this.data.imageUrl.isNotEmpty()
        && this.data.birthDate.isNotEmpty()
        && this.data.gender.isNotEmpty()
    ){
        UserResponse(
            id = this.id,
            data = parsedUser,
            publishDate = this.publishDate
        )
    }else{
        null
    }
}