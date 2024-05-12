package com.natiqhaciyef.prodocument.ui.view.main.home.options.protect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.FragmentProtectFileBinding
import com.natiqhaciyef.prodocument.ui.view.main.home.options.protect.contract.ProtectFileContract
import com.natiqhaciyef.prodocument.ui.view.main.home.options.protect.viewmodel.ProtectFileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class ProtectFileFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProtectFileBinding = FragmentProtectFileBinding::inflate,
    override val viewModelClass: KClass<ProtectFileViewModel> = ProtectFileViewModel::class
) : BaseFragment<FragmentProtectFileBinding, ProtectFileViewModel, ProtectFileContract.ProtectFileState, ProtectFileContract.ProtectFileEvent, ProtectFileContract.ProtectFileEffect>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}