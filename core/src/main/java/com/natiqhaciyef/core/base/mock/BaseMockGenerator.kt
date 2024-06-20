package com.natiqhaciyef.core.base.mock

import com.natiqhaciyef.common.constants.NOT_PRIMARY_CONSTRUCTOR_FOUND
import com.natiqhaciyef.common.constants.NOT_VALID_REQUEST
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


abstract class BaseMockGenerator<In, Out : Any> {
    abstract var createdMock: Out
    abstract var takenRequest: In

    fun createInstance(): BaseMockGenerator<In, Out> = this

    abstract fun getMock(
        request: In,
        action: (In) -> Out?
    ): Out


    companion object{
        class MockRequestException: Exception(NOT_VALID_REQUEST)
    }
}

fun <T : BaseMockGenerator<*, *>, In> generateMockerClass(
    mockClass: KClass<T>,
    request: In
): T {
    val primaryConstructor = mockClass.primaryConstructor
        ?: throw IllegalArgumentException(NOT_PRIMARY_CONSTRUCTOR_FOUND)
    return primaryConstructor.call(request)
}