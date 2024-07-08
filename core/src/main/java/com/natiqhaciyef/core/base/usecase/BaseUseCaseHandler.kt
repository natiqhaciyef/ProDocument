package com.natiqhaciyef.core.base.usecase

import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.constants.UNKNOWN_ERROR
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.core.base.network.NetworkResult
import kotlinx.coroutines.flow.flow


fun <OUT : Any, MT: Any> handleFlowResult(
    networkResult: suspend () -> NetworkResult<OUT>,
    operation: (OUT) -> Resource<MT>
) = flow {
    emit(Resource.loading())

    when(val result = networkResult.invoke()){
        is NetworkResult.Success -> {
            emit(operation.invoke(result.data))
        }

        is NetworkResult.Error -> {
            emit(
                Resource.error(
                    msg = result.message ?: UNKNOWN_ERROR,
                    data = null,
                    exception = Exception(result.message),
                    errorCode = result.code
                )
            )
        }

        is NetworkResult.Exception -> {
            emit(Resource.error(
                msg = result.e.message ?: SOMETHING_WENT_WRONG,
                data = null,
                exception = Exception(result.e),
                errorCode = -ONE
            ))
        }
    }
}