package com.natiqhaciyef.prodocument.ui.view.main.home.options.protect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.databinding.FragmentAddPasswordFileBinding
import com.natiqhaciyef.prodocument.ui.view.main.home.options.protect.contract.ProtectFileContract
import com.natiqhaciyef.prodocument.ui.view.main.home.options.protect.viewmodel.ProtectFileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class AddPasswordFileFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAddPasswordFileBinding = FragmentAddPasswordFileBinding::inflate,
    override val viewModelClass: KClass<ProtectFileViewModel> = ProtectFileViewModel::class
) : BaseFragment<FragmentAddPasswordFileBinding, ProtectFileViewModel, ProtectFileContract.ProtectFileState, ProtectFileContract.ProtectFileEvent, ProtectFileContract.ProtectFileEffect>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textConfiguration()
    }

    override fun onStateChange(state: ProtectFileContract.ProtectFileState) {
        when{
            state.isLoading -> {}
            else -> {}
        }
    }

    override fun onEffectUpdate(effect: ProtectFileContract.ProtectFileEffect) {

    }


    private fun textConfiguration(){
        with(binding){
            filePasswordConfirm.setPasswordTitleText(requireContext().getString(com.natiqhaciyef.common.R.string.confirm_password))
            filePasswordConfirm.setPasswordHintText(requireContext().getString(com.natiqhaciyef.common.R.string.confirm_password))
        }
    }
}