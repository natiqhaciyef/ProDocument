package com.natiqhaciyef.prodocument.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String,
    var email: String,
    var phoneNumber: String,
    var gender: String,
    var birthDate: String,
    var imageUrl: String,
    var password: String,
    var publishDate: String,
)