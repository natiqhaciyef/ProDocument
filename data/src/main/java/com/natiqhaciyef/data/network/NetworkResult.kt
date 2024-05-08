package com.natiqhaciyef.data.network

import retrofit2.HttpException
import retrofit2.Response

sealed class NetworkResult<T> {
    class Success<T : Any>(val data: T) : NetworkResult<T>()
    class Error<T : Any>(val code: Int, val message: String?) : NetworkResult<T>()
    class Exception<T : Any>(val e: Throwable) : NetworkResult<T>()
}

suspend fun <T : Any> handleNetworkResponse(
    mock: T? = null,
    handlingType: LoadType = LoadType.DEFAULT,
    execute: suspend () -> Response<T>
): NetworkResult<T> {
    val nullProperty = "Null property"

    return try {
        when (handlingType) {
            LoadType.MOCK -> {
                if (mock != null)
                    NetworkResult.Success(mock)
                else
                    throw NullPointerException(nullProperty)
            }

            LoadType.DEFAULT -> {
                val response = execute()
                val body = response.body()
                if (response.isSuccessful && body != null)
                    NetworkResult.Success(body)
                else
                    NetworkResult.Error(code = response.code(), message = response.message())
            }
        }
    } catch (e: HttpException) {
        NetworkResult.Error(code = e.code(), message = e.message())
    } catch (e: Throwable) {
        NetworkResult.Exception(e)
    }
}

enum class LoadType {
    MOCK,
    DEFAULT
}