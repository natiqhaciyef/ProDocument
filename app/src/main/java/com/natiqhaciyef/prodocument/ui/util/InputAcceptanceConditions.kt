package com.natiqhaciyef.prodocument.ui.util

import com.natiqhaciyef.common.constants.EIGHT
import com.natiqhaciyef.common.constants.ELEVEN
import com.natiqhaciyef.common.constants.FOUR
import com.natiqhaciyef.common.constants.SIX
import com.natiqhaciyef.common.constants.SPACE
import com.natiqhaciyef.common.constants.TEN
import com.natiqhaciyef.common.constants.THIRTEEN


object InputAcceptanceConditions {
    private const val PASSWORD_MIN_LENGTH = EIGHT
    private const val FULL_NAME_MIN_LENGTH = TEN
    private const val PHONE_NUMBER_MIN_LENGTH = THIRTEEN
    private const val NOT_SELECTED = "Not-selected"

    val emailRegex = Regex(
        pattern = "^[a-zA-Z0-9_!#$%&'*+/=?^`{|}~-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?^`{|}~-]+)*@(gmail|mail)\\.(ru|com)$",
        option = RegexOption.IGNORE_CASE
    )

    var phoneNumberRegex = Regex(
        pattern = "(\\+\\d( )?)?([-\\( ]\\d{3}[-\\) ])( )?\\d{3}-\\d{4}",
        option = RegexOption.IGNORE_CASE
    )

    fun checkGenderAcceptanceCondition(text: CharSequence?) =
        !text.isNullOrEmpty() && text != NOT_SELECTED

    fun checkPhoneAcceptanceCondition(text: CharSequence?) =
        !text.isNullOrEmpty() && text.length == PHONE_NUMBER_MIN_LENGTH

    fun checkFullNameAcceptanceCondition(text: CharSequence?) =
        !text.isNullOrEmpty() && text.length >= FULL_NAME_MIN_LENGTH

    fun checkEmailAcceptanceCondition(text: CharSequence?) =
        !text.isNullOrEmpty() //&& text.matches(emailRegex)

    fun checkPasswordAcceptanceCondition(text: CharSequence?) =
        !text.isNullOrEmpty() && text.length >= PASSWORD_MIN_LENGTH

    fun formatPhoneNumber(input: String): String {
        val formattedStringBuilder = StringBuilder()

        for (i in input.indices) {
            if ((input.length == FOUR || input.length == EIGHT || input.length == ELEVEN)
                && i == input.lastIndex && input[i] != ' '
            ) {
                formattedStringBuilder.append(SPACE)
            }

            if ((input.length == SIX || input.length == TEN || input.length == THIRTEEN)
                && i == input.lastIndex
            ) {
                formattedStringBuilder.removePrefix(SPACE)
            }

            formattedStringBuilder.append(input[i])
        }

        return formattedStringBuilder.toString()
    }
}

