package com.natiqhaciyef.data.mapper

import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.model.ui.UIResult
import com.natiqhaciyef.common.model.mapped.MappedUserModel
import com.natiqhaciyef.common.model.mapped.MappedUserWithoutPasswordModel
import com.natiqhaciyef.data.local.entity.UserEntity
import com.natiqhaciyef.data.network.response.UserResponse
import java.util.UUID


fun MappedUserModel.toMappedUserWithoutPassword(): MappedUserWithoutPasswordModel {
    return MappedUserWithoutPasswordModel(
        name = this.name,
        email = this.email,
        phoneNumber = this.phoneNumber,
        gender = this.gender,
        birthDate = this.birthDate,
        imageUrl = this.imageUrl,
        country = this.country,
        city = this.city,
        street = this.street,
    )
}

fun UserResponse.toMapped(): MappedUserModel {
    return MappedUserModel(
        name = this.fullName,
        phoneNumber = this.phoneNumber,
        gender = this.gender,
        birthDate = this.dateOfBirth,
        imageUrl = this.imageUrl,
        email = this.email,
        password = this.password,
        country = this.country,
        city = this.city,
        street = this.street,
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
            password = this.password,
            country = this.country,
            city = this.city,
            street = this.street,
        )

        UIResult(
            id = UUID.randomUUID().toString(),
            data = mappedUser,
            publishDate = this.publishDate,
            result = this.result?.toModel()
        )
    } else {
        null
    }
}


fun MappedUserModel.toResponse(): UserResponse =
    UserResponse(
        fullName = this.name,
        phoneNumber = this.phoneNumber,
        gender = this.gender,
        dateOfBirth = this.birthDate,
        imageUrl = this.imageUrl,
        email = this.email,
        password = this.password,
        country = this.country,
        city = this.city,
        street = this.street,
        publishDate = EMPTY_STRING,
        result = null
    )


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
            fullName = this.data.name,
            phoneNumber = this.data.phoneNumber,
            gender = this.data.gender,
            dateOfBirth = this.data.birthDate,
            imageUrl = this.data.imageUrl,
            email = this.data.email,
            password = this.data.password,
            country = this.data.country,
            city = this.data.city,
            street = this.data.street,
            publishDate = this.publishDate,
            result = this.result?.toResponse()
        )
    } else {
        null
    }
}