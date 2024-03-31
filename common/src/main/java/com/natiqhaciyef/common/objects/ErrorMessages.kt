package com.natiqhaciyef.common.objects

object ErrorMessages {
    const val CHECKED_PARAMETER_NOT_FOUND_EXCEPTION =
        "Checked parameter did not correct create or annotation did not add"
    const val CHECKED_USE_CASE_NOT_FOUND_EXCEPTION =
        "Checked UseCase did not correct create or annotation did not add"
    const val TOKEN_CREATION_FAILED_EXCEPTION =
        "Token creation failed due to network response returns null"
    const val TOKEN_REQUEST_FAILED =
        "Token request failed due to network request returns exception/null"
    const val OTP_REQUEST_FAILED =
        "OTP request failed due to network request returns exception/null"


    const val NULL_PROPERTY = "Null property"
    const val EMPTY_FIELD = "Empty field(s)"
    const val FINDING_EXCEPTION = "Finding failed"
    const val ELEMENT_NOT_FOUND = "Element not found"
    const val SOMETHING_WENT_WRONG = "Something went wrong"
    const val PERMISSION_DENIED = "Permission denied"
    const val INTERNET_CONNECTION_FAILED = "Internet connection failed"
    const val NOTIFICATION_NOT_SENT = "Notification not sent"

    const val SIGN_IN_FAILED = "Sign in failed"
    const val SIGN_UP_FAILED = "Sign up failed"
    const val USER_NOT_FOUND = "User not found"
    const val PASSWORD_RESETTING_FAILED = "Password resetting failed"
    const val WRONG_FILLED_FIELD = "Wrong filled field(s)"

    const val INVALID_TOKEN = "Invalid Token"

    const val DATA_NOT_FOUND = "Data not found"
    const val DOCUMENT_NOT_FOUND = "Document(s) not found"
    const val APPLICATION_UNDER_THE_TEST =
        "Application is under the test\nAfter production mode data will accessible"

    const val WRONG_FILLED_ALL_INPUTS_REASON =
        "Email is not correct filled or Password have to be over 8 character."
    const val WRONG_FILLED_EMAIL_INPUT_REASON = "Email is not correct filled"

    const val UNKNOWN_ERROR = "Unknown error"
    const val MAPPED_NULL_DATA = "Mapped null data"
    const val ERROR = "Error"

    const val DATE_OVER_FLOW_ERROR = "Selected date is not correct!"
    const val UPDATE_FAILED = "Update failed after unknown error!"
}