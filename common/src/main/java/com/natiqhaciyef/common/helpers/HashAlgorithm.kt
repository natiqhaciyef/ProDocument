package com.natiqhaciyef.common.helpers

import android.graphics.Bitmap
import android.util.Base64
import com.natiqhaciyef.common.constants.FORMATTED_ALGORITHM
import com.natiqhaciyef.common.constants.HUNDRED
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.security.MessageDigest


fun hashPassword(password: String): String{
    val shaTag = "SHA-256"
    val messageDigest = MessageDigest.getInstance(shaTag)
    val hashBytes = messageDigest.digest(password.toByteArray(StandardCharsets.UTF_8))
    val hexString = StringBuffer()

    for (byte in hashBytes){
        hexString.append(String.format(FORMATTED_ALGORITHM, byte))
    }

    return hexString.toString()
}

fun Bitmap.toResponseString(): String {
    val baos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, HUNDRED, baos)
    val b = baos.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
}