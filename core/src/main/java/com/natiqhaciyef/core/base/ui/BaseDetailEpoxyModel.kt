package com.natiqhaciyef.core.base.ui

import com.natiqhaciyef.common.model.UIResult

open class BaseDetailEpoxyModel<T>(
    val data: List<UIResult<T>>,
    val categories: List<String>,
)
