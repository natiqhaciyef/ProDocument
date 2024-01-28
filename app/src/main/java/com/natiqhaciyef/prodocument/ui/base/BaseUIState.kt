package com.natiqhaciyef.prodocument.ui.base


data class BaseUIState<T>(
    var data: T? = null,
    var list: List<T> = listOf(),
    var isLoading: Boolean = true,
    var isSuccess: Boolean = false,
    var isFail: Boolean = false,
    var message: String? = null,
    var isFailReason: Exception? = null,
) {


    fun onSuccess(
        action: (T?, String?) -> Unit = { _, _ -> }
    ) {
        action.invoke(data, message)
    }

    fun onFail(
        action: (T?, String?, Exception?) -> Unit = { _, _, _ -> }
    ) {
        action.invoke(data, message, isFailReason)
    }
}
