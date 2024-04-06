package com.natiqhaciyef.data.base.mock

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


abstract class BaseMockGenerator<In, Out : Any> {
    abstract var createdMock: Out
    abstract var takenRequest: In

    abstract fun createInstance(): BaseMockGenerator<In, Out>

    fun getMock(
        request: In,
        action: (In) -> Out?
    ): Out =
        if (request == takenRequest) { createdMock } else { action.invoke(request) ?: throw MockRequestException() }

    companion object{
        class MockRequestException: Exception("Not valid request")
    }
}

fun <T : BaseMockGenerator<*, *>, In> generateMockerClass(
    mockClass: KClass<T>,
    request: In
): T {
    val primaryConstructor = mockClass.primaryConstructor
        ?: throw IllegalArgumentException("No primary constructor found")
    return primaryConstructor.call(request)
}