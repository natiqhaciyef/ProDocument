package com.natiqhaciyef.prodocument.ui.base

import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

open class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    protected var _binding: VB? = null
    val binding: VB
        get() = _binding!!
}