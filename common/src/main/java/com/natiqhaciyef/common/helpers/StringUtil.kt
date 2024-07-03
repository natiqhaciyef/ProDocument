package com.natiqhaciyef.common.helpers

import com.google.gson.Gson
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.SPACE
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

fun String.secondWordFirstLetterLowercase(): String {
    val secondWord = this.findSecondWord().first.lowercaseFirstLetter()
    val secondWordFirstIndex = this.findSecondWord().second

    val modified = this.replaceRange(
        secondWordFirstIndex until secondWordFirstIndex + secondWord.length,
        secondWord
    )
    return modified
}

fun String.findSecondWord(): Pair<String, Int> {
    var isSecond = false
    var secondWord = EMPTY_STRING
    var indexOfFirstLetter = ZERO

    for (i in this.indices) {

        if (isSecond) {
            if (indexOfFirstLetter > i || indexOfFirstLetter == ZERO)
                indexOfFirstLetter = i
            secondWord += this[i]
        }

        if (this[i].toString() == EMPTY_STRING || this[i].toString() == SPACE) {
            isSecond = !isSecond

            if (!isSecond) {
                return Pair(secondWord.trim(), indexOfFirstLetter)
            }
        }
    }

    return Pair(secondWord.trim(), indexOfFirstLetter)
}

fun String.capitalizeFirstLetter(): String {
    val firstLetter = this[ZERO].toString().uppercase()[ZERO]
    return this.replaceFirst(this[ZERO], firstLetter)
}

fun String.lowercaseFirstLetter(): String {
    val firstLetter = this[ZERO].toString().lowercase()[ZERO]
    return this.replaceFirst(this[ZERO], firstLetter)
}