package com.natiqhaciyef.core.base.ui


interface UiState {
    var isLoading: Boolean
}

data class Idle<T>(val template: T) : UiState {
    override var isLoading: Boolean = false
}

interface UiEvent
interface UiEffect