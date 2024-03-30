package com.natiqhaciyef.domain.base.mock

abstract class BaseMockGenerator<In, Out> {
    abstract var createMock: Out
    abstract var setKey: String

    fun generateMock(defaultData: In): Out? = when (defaultData) {
        setKey -> { createMock }
        else -> null
    }
}