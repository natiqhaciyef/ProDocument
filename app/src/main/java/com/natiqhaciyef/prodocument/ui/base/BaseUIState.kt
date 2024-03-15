package com.natiqhaciyef.prodocument.ui.base


data class BaseUIState<T>(
    var obj: T? = null,
    var list: List<T> = listOf(),
    var isLoading: Boolean = false,
    var isSuccess: Boolean = false,
    var message: String? = null,
    var failReason: Exception? = null,
) {


    fun onSuccess(
        action: (T?, String?) -> Unit = { _, _ -> }
    ) {
        action.invoke(obj, message)
    }

    fun onFail(
        action: (T?, String?, Exception?) -> Unit = { _, _, _ -> }
    ) {
        action.invoke(obj, message, failReason)
    }
}
