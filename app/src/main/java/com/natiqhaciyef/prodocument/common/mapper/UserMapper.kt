package com.natiqhaciyef.prodocument.common.mapper

import com.natiqhaciyef.prodocument.common.helpers.hashPassword
import com.natiqhaciyef.prodocument.data.local.entity.UserEntity
import com.natiqhaciyef.prodocument.data.model.UserModel
import com.natiqhaciyef.prodocument.data.network.response.UserResponse
import com.natiqhaciyef.prodocument.domain.model.UIResult
import com.natiqhaciyef.prodocument.domain.model.mapped.MappedUserModel
import com.natiqhaciyef.prodocument.domain.model.mapped.MappedUserWithoutPasswordModel

// not hashable
fun UserModel.toMappedUser(hashed: Boolean): MappedUserModel {
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

fun MappedUserModel.toMappedUserWithoutPassword(): MappedUserWithoutPasswordModel {
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
fun MappedUserModel.toUser(hashed: Boolean): UserModel {
    return if (hashed)
        UserModel(
            name = this.name,
            email = this.email,
            phoneNumber = this.phoneNumber,
            gender = this.gender,
            birthDate = this.birthDate,
            imageUrl = this.imageUrl,
            password = this.password,
        )
    else
        UserModel(
            name = this.name,
            email = this.email,
            phoneNumber = this.phoneNumber,
            gender = this.gender,
            birthDate = this.birthDate,
            imageUrl = this.imageUrl,
            password = hashPassword(this.password),
        )
}


// Network
fun UserResponse.toUIResult(): UIResult<MappedUserModel>? {
    return if (
        this.email.isNotEmpty()
        && this.fullName.isNotEmpty()
        && this.password.isNotEmpty()
        && this.phoneNumber.isNotEmpty()
        && this.imageUrl.isNotEmpty()
        && this.dateOfBirth.isNotEmpty()
        && this.gender.isNotEmpty()
    ) {
        val mappedUser = MappedUserModel(
            name = this.fullName,
            email = this.email,
            phoneNumber = this.phoneNumber,
            gender = this.gender,
            birthDate = this.dateOfBirth,
            imageUrl = this.imageUrl,
            password = this.password
        )

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

    return if (
        this.data.email.isNotEmpty()
        && this.data.name.isNotEmpty()
        && this.data.password.isNotEmpty()
        && this.data.phoneNumber.isNotEmpty()
        && this.data.imageUrl.isNotEmpty()
        && this.data.birthDate.isNotEmpty()
        && this.data.gender.isNotEmpty()
    ) {
        UserResponse(
            id = this.id,
            fullName = this.data.name,
            phoneNumber = this.data.phoneNumber,
            gender = this.data.gender,
            dateOfBirth = this.data.birthDate,
            imageUrl = this.data.imageUrl,
            email = this.data.email,
            password = this.data.password,
            publishDate = this.publishDate,
            result = null
        )
    } else {
        null
    }
}


// Local db
fun UserEntity.toUIResult(): UIResult<MappedUserModel> {
    val mappedUser = MappedUserModel(
        name = this.name,
        email = this.email,
        phoneNumber = this.phoneNumber,
        gender = this.gender,
        birthDate = this.birthDate,
        imageUrl = this.imageUrl,
        password = this.password
    )

    return UIResult(
        id = this.id,
        data = mappedUser,
        publishDate = this.publishDate
    )
}

fun UIResult<MappedUserModel>.toEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        name = this.data.name,
        email = this.data.email,
        phoneNumber = this.data.phoneNumber,
        gender = this.data.gender,
        birthDate = this.data.birthDate,
        imageUrl = this.data.imageUrl,
        password = this.data.password,
        publishDate = this.publishDate
    )
}