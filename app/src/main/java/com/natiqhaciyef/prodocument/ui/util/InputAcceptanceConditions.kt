package com.natiqhaciyef.prodocument.ui.util


object InputAcceptanceConditions {
    private const val PASSWORD_MIN_LENGTH = 8
    private const val FULL_NAME_MIN_LENGTH = 10
    private const val PHONE_NUMBER_MIN_LENGTH = 13

    val emailRegex = Regex(
        pattern = "^[a-zA-Z0-9_!#$%&'*+/=?^`{|}~-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?^`{|}~-]+)*@(gmail|mail)\\.(ru|com)$",
        option = RegexOption.IGNORE_CASE
    )

    var phoneNumberRegex = Regex(
        pattern = "(\\+\\d( )?)?([-\\( ]\\d{3}[-\\) ])( )?\\d{3}-\\d{4}",
        option = RegexOption.IGNORE_CASE
    )

    fun checkGenderAcceptanceCondition(text: CharSequence?) =
        !text.isNullOrEmpty() && text != "Not-selected"

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
}

