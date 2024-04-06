package com.natiqhaciyef.domain.mapper

import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.common.helpers.hashPassword
import com.natiqhaciyef.common.model.UIResult
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
        publishDate = "",
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
            publishDate = this.publishDate,
            result = this.result?.toResponse()
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
        id = "${this.id}",
        data = mappedUser,
        publishDate = this.publishDate
    )
}

fun UIResult<MappedUserModel>.toEntity(): UserEntity {
    return UserEntity(
        id = this.id.toInt(),
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