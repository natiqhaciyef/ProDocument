package com.natiqhaciyef.common.constants

sealed interface ResultExceptions {

    data class UserLoadingFailed(
        val msg: String = USER_NOT_FOUND,
        val errorCode: Int = 404
    ) : Exception(), ResultExceptions

    data class UseCaseAnnotationNotFound(
        val msg: String = CHECKED_USE_CASE_NOT_FOUND_EXCEPTION,
        val errorCode: Int = 4042
    ) : Exception(), ResultExceptions

    data class TokenCreationFailed(
        val msg: String = TOKEN_CREATION_FAILED_EXCEPTION,
        val errorCode: Int = 400
    ) : Exception(), ResultExceptions

    data class RequestFailed(
        val msg: String = TOKEN_CREATION_FAILED_EXCEPTION,
        val errorCode: Int = 401
    ) : Exception(), ResultExceptions

    data class FieldsNotFound(
        val msg: String = EMPTY_FIELD,
        val errorCode: Int = 404
    ) : Exception(), ResultExceptions

    data class UnknownError(
        val msg: String = UNKNOWN_ERROR,
        val errorCode: Int = 400
    ) : Exception(), ResultExceptions

    data class CustomIOException(
        val msg: String = UNKNOWN_ERROR,
        val errorCode: Int = 400
    ) : Exception(), ResultExceptions

    data class BiometricAuthenticationError(
        val msg: String = BIOMETRIC_AUTHENTICATION_FAILED,
        val errorCode: Int = 401
    ) : Exception(), ResultExceptions

}