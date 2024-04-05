package com.natiqhaciyef.data.base.mock

import com.natiqhaciyef.data.network.NetworkResult


abstract class BaseMockGenerator<In, Out : Any>(
    var takenRequest: In,
    var setSuccessKey: String,
    var setErrorKey: String
) {
    abstract var createdMock: Out

    companion object {
        private const val MOCK_NON_CORRECT_REQUEST = "Request not correct"
        private const val MOCK_INTERNAL_SERVICE_ERROR_REQUEST = "Something went wrong"
        private const val MOCK_NOT_FOUND_ERROR_REQUEST = "Something went wrong"
    }


    fun generateMock(
        defaultData: String,
        request: In,
        action: (In) -> NetworkResult<Out>
    ): NetworkResult<Out> =
        if (request == takenRequest){
            when (defaultData) {
                setSuccessKey -> {
                    NetworkResult.Success(createdMock)
                }

                setErrorKey -> {
                    NetworkResult.Error(code = 500, message = MOCK_INTERNAL_SERVICE_ERROR_REQUEST)
                }

                else -> NetworkResult.Error(code = 404, message = MOCK_NOT_FOUND_ERROR_REQUEST)
            }
        }else{
            action.invoke(request)
        }
}