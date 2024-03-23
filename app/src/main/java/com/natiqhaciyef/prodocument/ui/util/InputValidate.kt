package com.natiqhaciyef.prodocument.ui.util

fun formatPhoneNumber(input: String): String {
    val formattedStringBuilder = StringBuilder()

    for (i in input.indices) {
        if ((input.length == 4 || input.length == 8 || input.length == 11)
            && i == input.lastIndex && input[i] != ' '
        ) {
            formattedStringBuilder.append(" ")
        }

        if ((input.length == 6 || input.length == 10 || input.length == 13)
            && i == input.lastIndex
        ) {
            formattedStringBuilder.removePrefix(" ")
        }

        formattedStringBuilder.append(input[i])
    }

    return formattedStringBuilder.toString()
}
