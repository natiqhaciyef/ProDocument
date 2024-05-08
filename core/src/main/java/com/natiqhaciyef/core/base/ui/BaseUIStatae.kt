package com.natiqhaciyef.core.base.ui


data class BaseUIState<T>(
    var obj: T? = null,
    var list: List<T> = listOf(),
    var isLoading: Boolean = false,
    var isSuccess: Boolean = false,
    var message: String? = null,
    var failReason: Exception? = null,
)


data class State<T>(
    val data: T, override var isLoading: Boolean
) : UiState



interface UiState{
    var isLoading: Boolean
}
interface UiEvent
interface UiEffect