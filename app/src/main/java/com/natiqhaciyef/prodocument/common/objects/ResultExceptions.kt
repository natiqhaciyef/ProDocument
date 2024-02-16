package com.natiqhaciyef.prodocument.common.objects

sealed interface ResultExceptions {

    data class CheckedParameterNotFound(
        val msg: String = ErrorMessages.CHECKED_PARAMETER_NOT_FOUND_EXCEPTION,
        val errorCode: Int = 4041
    ) : Exception(), ResultExceptions

    data class UseCaseAnnotationNotFound(
        val msg: String = ErrorMessages.CHECKED_USE_CASE_NOT_FOUND_EXCEPTION,
        val errorCode: Int = 4042
    ) : Exception(), ResultExceptions

    data class TokenCreationFailed(
        val msg: String = ErrorMessages.TOKEN_CREATION_FAILED_EXCEPTION,
        val errorCode: Int = 4043
    ) : Exception(), ResultExceptions

    data class TokenRequestFailed(
        val msg: String = ErrorMessages.TOKEN_CREATION_FAILED_EXCEPTION,
        val errorCode: Int = 5001
    ) : Exception(), ResultExceptions

}