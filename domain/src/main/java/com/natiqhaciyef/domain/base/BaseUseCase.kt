package com.natiqhaciyef.domain.base

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.UIResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@UseCase
abstract class BaseUseCase<REPO : BaseRepository, In : Any?, Out : Any?>(
    protected val repository: REPO
) : BaseUseCaseInterface<In, Out> {

    override fun invoke(): Flow<Resource<Out>>? {
        return null
    }

    override fun run(data: In): Flow<Resource<Boolean>> {
        return flow { emit(Resource.error(data = false)) }
    }

    override fun operate(data: In): Flow<Resource<Out>> {
        return flow { emit(Resource.error(data = null)) }
    }

    /***
     * Check annotation is an essential function for
     * defining parameter type in base and sub classes
     * constructor parameters.
     *
     * @return Boolean
     * @author Natig Hajiyev && ProScan app
     */
    protected fun <Z> checkAnnotation(
        type: Class<Z>,
    ): Boolean {
        return type.getAnnotation(UseCase::class.java) != null
    }
}
