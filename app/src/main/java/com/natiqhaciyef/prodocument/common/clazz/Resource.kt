package com.natiqhaciyef.prodocument.common.clazz

import com.natiqhaciyef.prodocument.common.objects.ErrorMessages
import kotlin.Exception


data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message: String?,
    val exception: Exception? = Exception()
) {
    companion object {
        fun <T> success(data: T?, message: String? = null): Resource<T & Any> {
            return Resource(Status.SUCCESS, data, message, null)
        }

        fun <T> error(
            msg: String = ErrorMessages.UNKNOWN_ERROR,
            data: T?,
            exception: Exception = Exception()
        ): Resource<T> {
            return Resource(Status.ERROR, data, msg, exception)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null, null)
        }
    }
}
