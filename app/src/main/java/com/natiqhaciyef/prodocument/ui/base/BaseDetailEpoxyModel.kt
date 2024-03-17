package com.natiqhaciyef.prodocument.ui.base

import com.natiqhaciyef.common.model.UIResult

open class BaseDetailEpoxyModel<T>(
    val data: List<UIResult<T>>,
    val categories: List<String>,
)
