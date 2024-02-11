package com.natiqhaciyef.prodocument.data.source

import com.natiqhaciyef.prodocument.data.model.UserIOModel
import com.natiqhaciyef.prodocument.data.network.service.UserService

class UserDataSource(
    private val service: UserService
) {

    suspend fun getUser(
        token: String,
        email: String,
        password: String,
    ) = service.getUser(
        token = token,
        email = email,
        password = password
    )

    suspend fun createAccount(
        userModel: UserIOModel
    ) = service.createAccount(
        fullname = userModel.name,
        phoneNumber = userModel.phoneNumber,
        gender = userModel.gender,
        dateOfBirth = userModel.birthDate,
        imageUrl = userModel.imageUrl,
        email = userModel.email,
        password = userModel.password
    )

}