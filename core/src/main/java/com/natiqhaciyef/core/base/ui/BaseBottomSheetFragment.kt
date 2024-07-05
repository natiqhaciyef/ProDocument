package com.natiqhaciyef.core.base.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetFragment<VB : ViewBinding, T: Any?>: BottomSheetDialogFragment() {
    abstract val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
    open var onItemClickAction: (T) -> Unit = {}
    open var onClickAction: () -> Unit = {}
    open var item: T? = null

    protected open var onDismissAndCancelAction : () -> Unit = {

    }

    protected var _binding: VB? = null
    val binding: VB
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindInflater(inflater, container, false)
        return binding.root
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        dialog.cancel()
        onDismissAndCancelAction()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dialog.cancel()
        onDismissAndCancelAction()
    }
}
