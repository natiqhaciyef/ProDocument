package com.natiqhaciyef.prodocument.ui.util

import android.text.method.PasswordTransformationMethod
import android.view.View

val emailRegex = Regex(
    pattern = "^[a-zA-Z0-9_!#$%&'*+/=?^`{|}~-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?^`{|}~-]+)*@(gmail|mail)\\.(ru|com)$",
    option = RegexOption.IGNORE_CASE
)

var phoneNumberRegex = Regex(
    pattern = "(\\+\\d( )?)?([-\\( ]\\d{3}[-\\) ])( )?\\d{3}-\\d{4}",
    option = RegexOption.IGNORE_CASE
)