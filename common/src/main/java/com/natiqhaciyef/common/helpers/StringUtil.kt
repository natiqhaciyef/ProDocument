package com.natiqhaciyef.common.helpers

import com.google.gson.Gson
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.model.mapped.MappedTokenModel


// Material
fun String.toMappedMaterial(): MappedMaterialModel {
    return Gson().fromJson(this, MappedMaterialModel::class.java)
}

fun MappedMaterialModel.toJsonString(): String {
    return Gson().toJson(this)
}


// Token
fun String.toMappedToken(): MappedTokenModel {
    return Gson().fromJson(this, MappedTokenModel::class.java)
}

fun MappedTokenModel.toJsonString(): String {
    return Gson().toJson(this)
}

fun String.capitalizeFirstLetter(): String{
    val firstLetter = this[ZERO].toString().uppercase()[ZERO]
    return this.replaceFirst(this[ZERO], firstLetter)
}