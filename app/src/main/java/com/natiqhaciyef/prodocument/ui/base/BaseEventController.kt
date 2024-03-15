package com.natiqhaciyef.prodocument.ui.base

import android.content.Context
import com.airbnb.epoxy.EpoxyController
import com.natiqhaciyef.common.model.UIResult

abstract class BaseEventController<T : BaseViewModel>(
    var context: Context,
    var viewModel: T?
) : EpoxyController() {

    companion object {
        fun <K> eventClickActionWithData(action: (UIResult<K>) -> Unit): (UIResult<K>) -> Unit {
            return action
        }
    }
}