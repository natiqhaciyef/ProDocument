package com.natiqhaciyef.prodocument.ui.util

val emailRegex = Regex(
    pattern = "^[a-zA-Z0-9_!#$%&'*+/=?^`{|}~-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?^`{|}~-]+)*@(gmail|mail)\\.(ru|com)$",
    option = RegexOption.IGNORE_CASE
)